package ConnectionManagment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.dnd.DropTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DeviceConnector {

    private DataObject dataToSend = new DataObject();
    public DataObject fetchedData= new DataObject();

    private Map<String,String> changeList = new HashMap<>();
    private Set<String> fetchingList = new HashSet<>();

    private Map<String,String> querys = new HashMap<>();

    private boolean connectionStatus;

    private String DeviceAddress;

    private Thread checkerThread = new Thread(()->statusChecker());

    public void openConection(){
        if(checkIfConnected())
        {
            connectionStatus = true;
            checkerThread.start();
        }
    }

    public void setConnectionAddress(String address)
    {
        if(!connectionStatus) {
            DeviceAddress = address;
        }
    }

    public void closeConnection()
    {
        connectionStatus = false;
        checkerThread.interrupt();
    }

    private boolean checkIfConnected() {
        querys.clear();
        querys.put(DataField.deviceID.toString(),"check");
        URL url = requestMake("Report.html",querys);
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                if(inputLine.contains("EPS32SmartSocket"))
                {
                    return true;
                }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void statusChecker()
    {
        while(checkIfConnected()) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                closeConnection();
                return;
            }
        }
        return;
    }

    public boolean getConnectionStatus()
    {
        return connectionStatus;
    }



    private void fetchData()
        {
        fetchedData = new DataObject();
        if(connectionStatus && checkIfConnected()) {

            querys.clear();
            querys.put(DataField.deviceID.toString(),"fetch");

            for (String par : fetchingList) {
                querys.put(par,"fetch");
            }
            fetchingList.clear();

            URL url = requestMake("Report.html",querys);
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))
            ) {
                Gson gson = new GsonBuilder().create();
                fetchedData = gson.fromJson(in, DataObject.class);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void fetchData(DataField...args)
    {
        for(DataField par:args)
        {
            fetchingList.add(par.toString());
        }
        fetchData();
    }

    public boolean sendData()
    {
        if(connectionStatus && checkIfConnected()) {
            URL url = requestMake("Update.html",changeList);
            changeList.clear();
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))
            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    if(inputLine.contains("Received Succesfully!"))
                    {
                        return true;
                    }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private URL requestMake(String urlPath, Map<String,String> queryList)
    {
        StringBuilder requestURL = new StringBuilder("http://");
        requestURL.append(DeviceAddress);
        requestURL.append("/");
        requestURL.append(urlPath);
        requestURL.append("?");
        for(Map.Entry<String,String> query: queryList.entrySet())
        {
            requestURL.append(query.getKey());
            requestURL.append("=");
            requestURL.append(query.getValue());
            requestURL.append("&");
        }
        requestURL.deleteCharAt(requestURL.length()-1);

        URL url = null;
        try {
            url = new URL(requestURL.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public void markChange(DataField name, String val)
    {
        changeList.put(name.toString(),val);
    }
    public void retractChange(String name)
    {
        changeList.remove(name);
    }
}
