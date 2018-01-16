package ConnectionManagment;



public class DataObject {
    private String deviceID;
    private String device_Status;
    private String mode;
    private String IP;
    private ssidLogin SSID_Data;

    private Settings settings;
    private class ssidLogin
    {
        public String SSID;
        public String PASSWORD;

    }
    private class Settings
    {
        public Integer wifiFailCount;
        public Integer sleepTime;
        public Integer sendRetryDelay;
    }


    public String getID() {
        return (deviceID==null)?(""):(deviceID);
    }

    public String getDevice_Status() {
        return (device_Status==null)?(""):(device_Status);
    }

    public String getMode() {
        return (mode==null)?(""):(mode);
    }

    public String getIP() {
        return (IP==null)?(""):(IP);
    }

    public String getSSID() {
        return (SSID_Data.SSID==null)?(""):(SSID_Data.SSID);
    }

    public String getPassword() {
        return (SSID_Data.PASSWORD==null)?(""):(SSID_Data.PASSWORD);
    }

}
