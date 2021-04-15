package signup.signup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import signup.signup.dao.CoordinatesDAO;
import signup.signup.dao.DeviceDAO;
import signup.signup.entity.Coordinates;
import signup.signup.entity.Device;
import signup.signup.main.maintest;
import signup.signup.service.AzureSend;
import signup.signup.service.DeviceService;
import signup.signup.service.ListenTopic;

import java.util.List;

@SpringBootApplication
public class RegisterAppApplication extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

//    @Autowired
//    DeviceDAO deviceDAO;
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RegisterAppApplication.class, args);


        //        CoordinatesDAO coordinatesDAO = context.getBean(CoordinatesDAO.class);
//        try {
////            ListenTopic listenTopic = context.getBean(ListenTopic.class);
////            listenTopic.receiveMessages();
////
////            DeviceService deviceService = context.getBean(DeviceService.class);
////            deviceService.createDevice();
////
////            AzureSend azureSend = new AzureSend("HostName=AndroidTest.azure-devices.net;DeviceId=devicetest1;SharedAccessKey=IpCmyJtrR8WqzE/4kws0cBJ4o+pvmYLFP+emeZq7rNI=");
////            azureSend.send();
//            double latitude = (Math.random() * 180.0) - 90.0;
//
//            double longitude = (Math.random() * 360.0) - 180.0;
//            double height = (Math.random() * 100);
//            latitude =Math.round(latitude*100)/100;
//            longitude =Math.round(longitude*100)/100;
//            height =Math.round(height*100)/100;
//            String deviceSend = "devicetest1";
//            Coordinates coor = new Coordinates();
//            coor.longtitude = longitude;
//            coor.deviceSend = deviceSend;
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String json = "{\n" +
//                    "  \"id\" : 0,\n" +
//                    "  \"deviceSend\" : \""+"devicetest523"+"\",\n" +
//                    "  \"idSend\" : "+"0"+"\n" +
//                    "}";
//            System.out.println(json);
//            final byte[] utf8Bytes = json.getBytes("UTF-8");
//            System.out.println(utf8Bytes.length);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //request hop le
        http.authorizeRequests()
                .antMatchers("/login", "/logout", "/loginError", "/assets/**", "/ckeditor/**", "/css_customize/**", "/global_assets/**", "/js_templates/**").permitAll()
//                .antMatchers("/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll();
        // .deleteCookies("JSESSIONID").invalidateHttpSession(true);
    }
}
