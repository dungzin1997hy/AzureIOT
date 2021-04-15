package signup.signup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import signup.signup.dao.DeviceDAO;
import signup.signup.dao.MessageSendDAO;
import signup.signup.entity.Device;
import signup.signup.entity.MessageSend;
import signup.signup.service.AzureSend;
import signup.signup.service.ControlDevice;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    ControlDevice controlDevice;

    @Autowired
    DeviceDAO deviceDAO;


    @Autowired
    MessageSendDAO messageSendDAO;

    @PostMapping("/controlDevice")
    public String controlDevice() {
        controlDevice.Control();
        return "ControlSuccess";
    }

    @PostMapping("/sendMessage")
    public String sendMessage() {
        try {
            List<Device> list = deviceDAO.getAllDevice();

            for (int i = 0; i < 1; i++) {
                int finalI = i;
                new Thread(() -> {
                    try {
                        Device device = list.get(finalI);
                        AzureSend azureSend = new AzureSend(messageSendDAO,device.connectionString,device.deviceId);
                        azureSend.send();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Sending message";
    }
}
