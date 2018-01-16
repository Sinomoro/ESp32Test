package ConnectionManagment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class DeviceFinder {
    private int curretnAddress = 0;
    private String machineIP;
    private String validDeviceIP;

    public DeviceFinder()
    {
        String temp = "127.0.0.1";
        try {

            temp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        machineIP=temp.substring(0,temp.lastIndexOf("."));
    }
    public void setDefaultSubnet(String subnet)
    {
        machineIP = subnet;
    }
    //veliau sutvarkyt su Uri
    public  String scanNetwork() {
        int timeout=10;
        for (int i=1;i<256;i++){
            String hostIP=machineIP + "." + (i+curretnAddress)%255;
            String request="http://"+hostIP+ "/Report.html?deviceID=fetch";
            URL url = null;
            try {
                url = new URL(request);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection con = null;
            try {
                con = url.openConnection();
                con.setConnectTimeout(10);
                con.setReadTimeout(1000);
                try (
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))
                ) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null)
                        if(inputLine.contains("EPS32SmartSocket"))
                        {
                            curretnAddress = i;
                            return hostIP;
                        }
                }
            } catch (IOException e) {
                //Nebuvo galiam prisijungti prie ip, tokio ip rpietaiso nebuvo arba tiesiog neprieme prisijungimu
            }
        }
        return "null";
    }
}
