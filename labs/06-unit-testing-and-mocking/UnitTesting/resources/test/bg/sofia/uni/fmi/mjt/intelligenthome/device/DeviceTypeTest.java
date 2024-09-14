package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.BULB;
import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.SMART_SPEAKER;
import static bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType.THERMOSTAT;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceTypeTest {
    @Test
    public void testDeviceTypeGetShortNameIsCorrect() {
        assertEquals(SMART_SPEAKER.getShortName(), "SPKR");
        assertEquals(BULB.getShortName(), "BLB");
        assertEquals(THERMOSTAT.getShortName(), "TMST");
    }
}
