package ConnectionManagment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class DeviceConnectorTest {
    DeviceConnector deviceConnector;

    @Before
    public void setUp() {

        DeviceFinder finder =new DeviceFinder();
        String address = "null";
        while(address == "null")
        {
            address = finder.scanNetwork();
        }
        deviceConnector = new DeviceConnector();
        deviceConnector.setConnectionAddress(address);
        deviceConnector.openConection();
    }

    @After
    public void tearDown() {
        deviceConnector.closeConnection();
    }

    @Test
    public void openConection() {
        deviceConnector.fetchData();
        String result = deviceConnector.fetchedData.getID();
        assertTrue(result.contains("EPS32SmartSocket"));
    }

    @Test
    public void closeConnection() {
        deviceConnector.closeConnection();
        deviceConnector.fetchData();
        String result = deviceConnector.fetchedData.getID();
        assertTrue(result.equalsIgnoreCase(""));
    }

    @Test
    public void getConnectionStatus() {
        assertTrue(deviceConnector.getConnectionStatus());
        deviceConnector.closeConnection();
        assertFalse(deviceConnector.getConnectionStatus());
    }

    @Test
    public void setConnectionAddress() {
        deviceConnector.setConnectionAddress("bad Address");
        deviceConnector.fetchData();
        String result = deviceConnector.fetchedData.getID();
        assertTrue(result.contains("EPS32SmartSocket"));
    }

    @Test
    public void fetchData() {
        deviceConnector.markChange(DataField.SSID,"SSID_Name");
        deviceConnector.markChange(DataField.password,"password");
        deviceConnector.sendData();
        deviceConnector.fetchData(DataField.SSID,DataField.password);
        assertEquals("SSID_Name",deviceConnector.fetchedData.getSSID());
        assertEquals("password",deviceConnector.fetchedData.getPassword());
    }

    @Test
    public void sendData() {
        deviceConnector.markChange(DataField.SSID,"NaujasSSID");

        deviceConnector.sendData();
        deviceConnector.fetchData(DataField.SSID);

        assertEquals("NaujasSSID",deviceConnector.fetchedData.getSSID());

        deviceConnector.markChange(DataField.password,"NaujasPassword");

        deviceConnector.sendData();
        deviceConnector.fetchData(DataField.SSID,DataField.password);

        assertEquals("NaujasSSID",deviceConnector.fetchedData.getSSID());
        assertEquals("NaujasPassword",deviceConnector.fetchedData.getPassword());

        deviceConnector.markChange(DataField.SSID,"GerasSSID");
        deviceConnector.markChange(DataField.password,"blogasPass");
        deviceConnector.retractChange("password");

        deviceConnector.sendData();
        deviceConnector.fetchData(DataField.SSID,DataField.password);

        assertEquals("GerasSSID",deviceConnector.fetchedData.getSSID());
        assertEquals("NaujasPassword",deviceConnector.fetchedData.getPassword());
    }
}