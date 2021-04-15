package signup.signup.service;

import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import signup.signup.dao.CoordinatesDAO;
import signup.signup.dao.WeatherDAO;
import signup.signup.entity.Coordinates;
import signup.signup.entity.Weather;
import signup.signup.utils.Util;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Component
public class ListenTopic {

    @Autowired
    CoordinatesDAO coorDAO;

    @Autowired
    WeatherDAO weaDAO;




    public void receiveMessages() throws InterruptedException {
        CountDownLatch countdownLatch = new CountDownLatch(1);

        // Create an instance of the processor through the ServiceBusClientBuilder
        ServiceBusProcessorClient processorClient = new ServiceBusClientBuilder()
                .connectionString(Util.CONNECTIONSTRING)
                .processor()
                .topicName(Util.TOPICNAME)
                .subscriptionName(Util.SUBNAME)
                .processMessage(context1 ->processMessage(context1))
                .processError(context -> processError(context, countdownLatch))
                .buildProcessorClient();

        System.out.println("Starting the processor");
        processorClient.start();

    }

    public void processMessage(ServiceBusReceivedMessageContext context) {
        ServiceBusReceivedMessage message = context.getMessage();
        System.out.printf("Contents: %s%n", message.getBody());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Coordinates emp = objectMapper.readValue(String.valueOf(message.getBody()), Coordinates.class);
            LocalDateTime time = LocalDateTime.now();
            emp.time = time;
            coorDAO.addCoor(emp);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            Weather weather = objectMapper.readValue(String.valueOf(message.getBody()), Weather.class);
            LocalDateTime time = LocalDateTime.now();
            weather.time = time;
            weaDAO.addWeather(weather);
        }catch (Exception e){

        }
    }


    public static void processError(ServiceBusErrorContext context, CountDownLatch countdownLatch) {
        System.out.printf("Error when receiving messages from namespace: '%s'. Entity: '%s'%n",
                context.getFullyQualifiedNamespace(), context.getEntityPath());

        if (!(context.getException() instanceof ServiceBusException)) {
            System.out.printf("Non-ServiceBusException occurred: %s%n", context.getException());
            return;
        }

        ServiceBusException exception = (ServiceBusException) context.getException();
        ServiceBusFailureReason reason = exception.getReason();

        if (reason == ServiceBusFailureReason.MESSAGING_ENTITY_DISABLED
                || reason == ServiceBusFailureReason.MESSAGING_ENTITY_NOT_FOUND
                || reason == ServiceBusFailureReason.UNAUTHORIZED) {
            System.out.printf("An unrecoverable error occurred. Stopping processing with reason %s: %s%n",
                    reason, exception.getMessage());

            countdownLatch.countDown();
        } else if (reason == ServiceBusFailureReason.MESSAGE_LOCK_LOST) {
            System.out.printf("Message lock lost for message: %s%n", context.getException());
        } else if (reason == ServiceBusFailureReason.SERVICE_BUSY) {
            try {
                // Choosing an arbitrary amount of time to wait until trying again.
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Unable to sleep for period of time");
            }
        } else {
            System.out.printf("Error source %s, reason %s, message: %s%n", context.getErrorSource(),
                    reason, context.getException());
        }
    }


}
