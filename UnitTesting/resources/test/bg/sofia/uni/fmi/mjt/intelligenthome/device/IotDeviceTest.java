package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IotDeviceTest {

    @Test
    public void testGetName() {
        IoTDevice device = new AmazonAlexa("Alexa", 20, LocalDateTime.now());

        assertEquals("Alexa", device.getName());
    }

    @Test
    public void testGetPowerConsumption() {
        IoTDevice device = new AmazonAlexa("Alexa", 20, LocalDateTime.now());

        assertEquals(20, device.getPowerConsumption());
    }
}
