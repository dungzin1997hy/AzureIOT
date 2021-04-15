package signup.signup.service;
import com.microsoft.azure.sdk.iot.service.devicetwin.DeviceMethod;
import com.microsoft.azure.sdk.iot.service.devicetwin.MethodResult;
import com.microsoft.azure.sdk.iot.service.exceptions.IotHubException;
import org.springframework.stereotype.Component;
import signup.signup.utils.Util;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class ControlDevice {

    public void Control(){
        try {
            System.out.println("Calling direct method...");

            // Create a DeviceMethod instance to call a direct method.
            DeviceMethod methodClient = DeviceMethod.createFromConnectionString(Util.CONNECTIONSTRINGDEVICE);

            // Call the direct method.
            MethodResult result = methodClient.invoke(Util.deviceID, Util.methodName, Util.responseTimeout, Util.connectTimeout, Util.payload);

            if (result == null) {
                throw new IOException("Direct method invoke returns null");
            }

            // Show the acknowledgement from the device.
            System.out.println("Status: " + result.getStatus());
            System.out.println("Response: " + result.getPayload());
        } catch (IotHubException e) {
            System.out.println("IotHubException calling direct method:");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException calling direct method:");
            System.out.println(e.getMessage());
        }
        System.out.println("Done!");
    }
}
