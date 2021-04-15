package signup.signup.utils;

import java.util.concurrent.TimeUnit;

public class Util {

    public static final String deviceID = "device1";
    public static final String CONNECTIONSTRING = "Endpoint=sb://servicebusandroid.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=aR52XzkdxlZztTGfhpNbgK/8vCMXW+dU+6Y8b3vG5j0=";
    public static final String CONNECTIONSTRINGDEVICE = "HostName=AndroidTest.azure-devices.net;SharedAccessKeyName=iothubowner;SharedAccessKey=5vwBJjQVOq4bb2xvmCj3ZvuqBvP/y5J80JlDUH1ruRM=";

    public static final String TOPICNAME = "mytopic";
    public static final String SUBNAME = "1111";
    public static final Long responseTimeout = TimeUnit.SECONDS.toSeconds(30);
    public static final Long connectTimeout = TimeUnit.SECONDS.toSeconds(5);
    public static final int payload = 10;
    public static final String methodName ="ChangeMessageType";
}
