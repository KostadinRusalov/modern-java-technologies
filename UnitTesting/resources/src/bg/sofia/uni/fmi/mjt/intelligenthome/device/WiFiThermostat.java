package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.LocalDateTime;

public class WiFiThermostat extends IoTDeviceBase {

    public WiFiThermostat(String name, double powerConsumption, LocalDateTime installationDateTime) {
        super(DeviceType.THERMOSTAT, name, powerConsumption, installationDateTime);
    }
}
