package signup.signup.service;

import com.google.gson.Gson;
import com.microsoft.azure.sdk.iot.device.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import signup.signup.entity.Device;
import signup.signup.utils.CmdUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Random;

@Component
public class DeviceService {
    @Autowired
    CmdUtil cmdUtil;

    private static IotHubClientProtocol protocol = IotHubClientProtocol.MQTT;

    public void createDevice() {
        try {
            for (int i = 999; i < 1000; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        String deviceId = "devicetest" + finalI;
                        String cmd = "az iot hub device-identity create --device-id " + deviceId + " --hub-name AndroidTest";
                        cmdUtil.runCmd(cmd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllConnectionString(){
        try {
            for (int i = 19; i < 1000; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        String deviceId = "devicetest" + finalI;
                        String cmd = "az iot hub device-identity show-connection-string --device-id "+deviceId+" --hub-name AndroidTest";
                        cmdUtil.getAllConnectionString(cmd,deviceId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String deviceId, String connectionString){
        try {
            // Initialize the simulated telemetry.
            double minTemperature = 20;
            double minHumidity = 60;
            Random rand = new Random();
            DeviceClient client = new DeviceClient(connectionString,protocol);
            client.open();
            while (true) {
                // Simulate telemetry.
                double currentTemperature = minTemperature + rand.nextDouble() * 15;
                double currentHumidity = minHumidity + rand.nextDouble() * 20;
                double windspeed = minHumidity + rand.nextDouble() * 20;
                DeviceService.TelemetryDataPoint telemetryDataPoint = new DeviceService.TelemetryDataPoint();
                telemetryDataPoint.temperature = currentTemperature;
                telemetryDataPoint.humidity = currentHumidity;
                telemetryDataPoint.windSpeed = currentHumidity;

                // Add the telemetry to the message body as JSON.
                String msgStr = telemetryDataPoint.serialize();
                Message msg = new Message(msgStr);

                // Add a custom application property to the message.
                // An IoT hub can filter on these properties without access to the message body.
                msg.setProperty("temperatureAlert", (currentTemperature > 30) ? "true" : "false");

                System.out.println("Sending message: " + msgStr);

                Object lockobj = new Object();

                // Send the message.
                DeviceService.EventCallback callback = new DeviceService.EventCallback();
                client.sendEventAsync(msg, callback, lockobj);

                synchronized (lockobj) {
                    lockobj.wait();
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Finished.");
        }
    }
    private static class TelemetryDataPoint {
        public double temperature;
        public double humidity;

        public double windSpeed;

        // Serialize object to JSON format.
        public String serialize() {
            Gson gson = new Gson();
            return gson.toJson(this);
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
}
