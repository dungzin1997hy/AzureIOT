package signup.signup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import signup.signup.dao.DeviceDAO;
import signup.signup.entity.Device;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class CmdUtil {

    @Autowired
    DeviceDAO deviceDAO;

    public void runCmd(String cmd) {
        System.out.println(cmd);
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
            builder.redirectErrorStream(true);
            //builder.directory(new File(folder));
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                System.out.println(line);
                if (line == null) {
                    break;
                } else {
                }
            }
            p.destroy();
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllConnectionString(String cmd,String deviceId){
        System.out.println(cmd);
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", cmd);
            builder.redirectErrorStream(true);
            //builder.directory(new File(folder));
            Process p = builder.start();

            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                System.out.println(line);
                if (line == null) {
                    break;
                } else {
                    if(line.contains("connectionString")){
                        line = "{\n"+line +"\n}";
                        ObjectMapper objectMapper = new ObjectMapper();
                        Device device = objectMapper.readValue(String.valueOf(line),Device.class);
                        device.deviceId = deviceId;
                        deviceDAO.addDevice(device);
                    }
                }
            }
            p.destroy();
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
