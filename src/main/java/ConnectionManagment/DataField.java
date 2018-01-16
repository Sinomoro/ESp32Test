package ConnectionManagment;

public enum DataField {

    SSID("SSID"),
    password("password"),
    deviceID("deviceID");

    private String name;

    DataField(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
