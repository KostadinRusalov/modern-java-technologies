package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDeviceBase;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {

    @Mock
    private DeviceStorage storage;

    @InjectMocks
    private IntelligentHomeCenter homeCenter;

    private IoTDevice device;

    @BeforeEach
    public void setUp() {
        device = new AmazonAlexa("Alexa", 20, LocalDateTime.now());
        IoTDeviceBase.startIncrementing();
    }

    @Test
    public void testRegisterNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.register(null));
    }

    @Test
    public void testUnRegisterNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.unregister(null));
    }

    @Test
    public void testGetDeviceByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(null));
    }

    @Test
    public void testGetDeviceByIdBlank() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getDeviceById(" "));
    }

    @Test
    public void testRegisterUnregisteredDevice() throws DeviceAlreadyRegisteredException {
        homeCenter.register(device);

        verify(storage, times(1)).store(device.getId(), device);
    }

    @Test
    public void testRegisterRegisteredDevice() {
        when(storage.exists(anyString())).thenReturn(true);

        assertThrows(DeviceAlreadyRegisteredException.class, () -> homeCenter.register(device));
        verify(storage, never()).store(device.getId(), device);
    }

    @Test
    public void testUnregisterRegisteredDevice() throws DeviceNotFoundException {
        when(storage.exists(anyString())).thenReturn(true);

        homeCenter.unregister(device);
        verify(storage, times(1)).delete(device.getId());
    }

    @Test
    public void testUnregisterUnregisteredDevice() {
        when(storage.exists(anyString())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> homeCenter.unregister(device));
        verify(storage, never()).delete(device.getId());
    }

    @Test
    public void testGetDeviceByIdThatExists() throws DeviceNotFoundException {
        when(storage.exists(anyString())).thenReturn(true);
        when(storage.get(anyString())).thenReturn(device);

        assertEquals(device, homeCenter.getDeviceById(device.getId()));
    }


    @Test
    public void testGetDeviceByIdThatNotExists() {
        when(storage.exists(anyString())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> homeCenter.getDeviceById(device.getId()));
        verify(storage, never()).get(device.getId());
    }

    @Test
    public void testGetDeviceQuantity() {
        when(storage.listAll()).thenReturn(List.of(
                new RgbBulb("", 20, LocalDateTime.now()),
                new AmazonAlexa("Alexa", 20, LocalDateTime.now()),
                new AmazonAlexa("Alexa2", 20, LocalDateTime.now()),
                new RgbBulb("Nicola Tesla", 20, LocalDateTime.now()),
                new WiFiThermostat("WiFi", 20, LocalDateTime.now()),
                new AmazonAlexa("Alexa3", 20, LocalDateTime.now())
            )
        );

        assertEquals(3, homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));
        assertEquals(2, homeCenter.getDeviceQuantityPerType(DeviceType.BULB));
        assertEquals(1, homeCenter.getDeviceQuantityPerType(DeviceType.THERMOSTAT));
    }

    @Test
    public void testGetDeviceQuantityWhenNoDevices() {
        when(storage.listAll()).thenReturn(Collections.emptyList());

        assertEquals(0, homeCenter.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER));
    }

    @Test
    public void testGetTopNegativeNDevices() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getTopNDevicesByPowerConsumption(-1));
    }

    @Test
    public void testGetTopNAll() {
        List<IoTDevice> all = List.of(
            new RgbBulb("", 200, LocalDateTime.now()),
            new AmazonAlexa("Alexa", 210, LocalDateTime.now()),
            new AmazonAlexa("Alexa2", 13, LocalDateTime.now()),
            new RgbBulb("Nicola Tesla", 120, LocalDateTime.now()),
            new WiFiThermostat("WiFi", 180, LocalDateTime.now()),
            new AmazonAlexa("Alexa3", 90, LocalDateTime.now())
        );

        List<String> expected = List.of(
            "BLB--0",
            "SPKR-Alexa-1",
            "SPKR-Alexa2-2",
            "BLB-Nicola Tesla-3",
            "TMST-WiFi-4",
            "SPKR-Alexa3-5"
        );

        when(storage.listAll()).thenReturn(all);

        assertIterableEquals(expected, homeCenter.getTopNDevicesByPowerConsumption(100));
    }

    @Test
    public void testGetTopNFirst() {
        List<IoTDevice> all = List.of(
            new RgbBulb("Edison", 1, LocalDateTime.now()),
            new AmazonAlexa("Alexa", 210, LocalDateTime.now()),
            new AmazonAlexa("Alexa2", 13, LocalDateTime.now()),
            new RgbBulb("Nicola Tesla", 120, LocalDateTime.now()),
            new WiFiThermostat("WiFi", 180, LocalDateTime.now()),
            new AmazonAlexa("Alexa3", 90, LocalDateTime.now())
        );

        List<String> expected = List.of("BLB-Edison-0", "SPKR-Alexa-1", "SPKR-Alexa2-2");

        when(storage.listAll()).thenReturn(all);

        assertIterableEquals(expected, homeCenter.getTopNDevicesByPowerConsumption(3));
    }

    @Test
    public void testFirstNegativeNDevices() {
        assertThrows(IllegalArgumentException.class, () -> homeCenter.getFirstNDevicesByRegistration(-1));
    }

    @Test
    public void testFirstNAll() throws DeviceAlreadyRegisteredException {
        List<IoTDevice> all = List.of(
            new RgbBulb("Edison", 1, LocalDateTime.now()),
            new AmazonAlexa("Alexa", 210, LocalDateTime.now()),
            new AmazonAlexa("Alexa2", 13, LocalDateTime.now()),
            new RgbBulb("Nicola Tesla", 120, LocalDateTime.now()),
            new WiFiThermostat("WiFi", 180, LocalDateTime.now()),
            new AmazonAlexa("Alexa3", 90, LocalDateTime.now())
        );

        for (var device : all) {
            homeCenter.register(device);
        }

        when(storage.listAll()).thenReturn(all);

        assertIterableEquals(all, homeCenter.getFirstNDevicesByRegistration(10));
    }

    @Test
    public void testFirstNSome() throws DeviceAlreadyRegisteredException {
        List<IoTDevice> all = List.of(
            new RgbBulb("Edison", 1, LocalDateTime.now()),
            new AmazonAlexa("Alexa", 210, LocalDateTime.now()),
            new AmazonAlexa("Alexa2", 13, LocalDateTime.now()),
            new RgbBulb("Nicola Tesla", 120, LocalDateTime.now()),
            new WiFiThermostat("WiFi", 180, LocalDateTime.now()),
            new AmazonAlexa("Alexa3", 90, LocalDateTime.now())
        );

        for (var device : all) {
            homeCenter.register(device);
        }

        when(storage.listAll()).thenReturn(all);

        assertIterableEquals(List.of(all.get(0), all.get(1), all.get(2)), homeCenter.getFirstNDevicesByRegistration(3));
    }

}
