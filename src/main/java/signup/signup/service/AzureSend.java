package signup.signup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import signup.signup.dao.Impl.MessageSendDAOImpl;
import signup.signup.dao.MessageSendDAO;
import signup.signup.entity.Coordinates;
import signup.signup.entity.MessageSend;

import java.io.*;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Component
public class AzureSend {

    private MessageSendDAO messDAO;


    private String connString;
    private String deviceId;
    // Using the MQTT protocol to connect to IoT Hub
    private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;
    public DeviceClient client;

    public AzureSend(String connString) throws URISyntaxException {
        this.connString = connString;
        client = new DeviceClient(connString, protocol);
    }

    public AzureSend(String connString, String deviceId) throws URISyntaxException {
        this.connString = connString;
        this.deviceId = deviceId;
        client = new DeviceClient(connString, protocol);
    }

    public AzureSend(MessageSendDAO messDAO, String connString, String deviceId) throws URISyntaxException {
        this.messDAO = messDAO;
        this.connString = connString;
        this.deviceId = deviceId;
        client = new DeviceClient(connString, protocol);
    }

    public AzureSend() {
    }

    // Define method response codes
    private static final int METHOD_SUCCESS = 200;
    private static final int METHOD_NOT_DEFINED = 404;
    private static final int INVALID_PARAMETER = 400;

    private static int telemetryInterval = 1000;

    // Specify the telemetry to send to your IoT hub.
    private static class TelemetryDataPoint {
        public double temperature;
        public double humidity;

        // Serialize object to JSON format.
        public String serialize() {
            Gson gson = new Gson();
            return gson.toJson(this);
        }
    }

    // Print the acknowledgement received from IoT Hub for the method acknowledgement sent.
    protected static class DirectMethodStatusCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("Direct method # IoT Hub responded to device method acknowledgement with status: " + status.name());
        }
    }

    // Print the acknowledgement received from IoT Hub for the telemetry message sent.
    private static class EventCallback implements IotHubEventCallback {
        public void execute(IotHubStatusCode status, Object context) {
            System.out.println("IoT Hub responded to message with status: " + status.name());

            if (context != null) {
                synchronized (context) {
                    context.notify();
                }
            }
        }
    }

    protected static class DirectMethodCallback implements DeviceMethodCallback {
        private void setTelemetryInterval(int interval) {
            System.out.println("Direct method # Setting telemetry interval (seconds): " + interval);
            telemetryInterval = interval * 1000;
        }

        @Override
        public DeviceMethodData call(String methodName, Object methodData, Object context) {
            DeviceMethodData deviceMethodData;
            String payload = new String((byte[]) methodData);
            switch (methodName) {
                case "SetTelemetryInterval": {
                    int interval;
                    try {
                        int status = METHOD_SUCCESS;
                        interval = Integer.parseInt(payload);
                        System.out.println(payload);
                        setTelemetryInterval(interval);
                        deviceMethodData = new DeviceMethodData(status, "Executed direct method " + methodName);
                    } catch (NumberFormatException e) {
                        int status = INVALID_PARAMETER;
                        deviceMethodData = new DeviceMethodData(status, "Invalid parameter " + payload);
                    }
                    break;
                }
                default: {
                    int status = METHOD_NOT_DEFINED;
                    deviceMethodData = new DeviceMethodData(status, "Not defined direct method " + methodName);
                }
            }
            return deviceMethodData;
        }
    }


    public class MessageSender implements Runnable {
        public String deviceId;
        private MessageSendDAO messDAO;

        public MessageSender(String deviceId) {
            this.deviceId = deviceId;
        }

        public MessageSender(String deviceId, MessageSendDAO messDAO) {
            this.deviceId = deviceId;
            this.messDAO = messDAO;
        }

        public void run() {
            try {
                // Initialize the simulated telemetry.
                double minTemperature = 20;
                double minHumidity = 60;
                Random rand = new Random();
                int i=1;
                while (i>0) {
                    // Simulate telemetry.
                    i--;
                    String json = "{\n" +
                            "  \"id\" : 0,\n" +
                            "  \"deviceSend\" : \""+this.deviceId+"\",\n" +
                            "  \"idSend\" : "+i+"\n" +
                            "}";

                    Message msg = new Message(json);
                    // Add a custom application property to the message.
                    // An IoT hub can filter on these properties without access to the message body.

                    System.out.println("Sending message: " + json);

                    Object lockobj = new Object();

                    // Send the message.
                    EventCallback callback = new EventCallback();

                    MessageSend messageSend = new MessageSend();
                    messageSend.deviceId = this.deviceId;
                    messageSend.idSend =i+"";
                    LocalDateTime time = LocalDateTime.now();
                    messageSend.time = time;
                    messDAO.addMessageSend(messageSend);
                    client.sendEventAsync(msg, callback, lockobj);

                    synchronized (lockobj) {
                        lockobj.wait();
                    }
                    Thread.sleep(8000);
                }
            } catch (InterruptedException e) {
                System.out.println("Finished.");
            }
        }
    }

    public void send() throws IOException, URISyntaxException {

        // Connect to the IoT hub.

        client.open();

        // Register to receive direct method calls.
        client.subscribeToDeviceMethod(new DirectMethodCallback(), null, new DirectMethodStatusCallback(), null);

        // Create new thread and start sending messages
        MessageSender sender = new MessageSender(this.deviceId,this.messDAO);
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(sender);

        // Stop the application.
        System.out.println("Press ENTER to exit.");
        System.in.read();
        executor.shutdownNow();
        client.closeNow();
    }
}
