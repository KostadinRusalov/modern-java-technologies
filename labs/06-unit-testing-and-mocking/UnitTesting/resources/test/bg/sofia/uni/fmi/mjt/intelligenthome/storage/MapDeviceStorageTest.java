package bg.sofia.uni.fmi.mjt.intelligenthome.storage;


import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapDeviceStorageTest {
    private MapDeviceStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new MapDeviceStorage();
    }

    @Test
    public void testGet() {
        assertNull(storage.get("1"));
    }

    @Test
    public void testGetReally() {
        var device = new RgbBulb("Bobo", 20, LocalDateTime.now());
        storage.store("1", device);
        assertEquals(device, storage.get("1"));
    }

    @Test
    public void testExists() {
        assertFalse(storage.exists("1"));
    }

    @Test
    public void testDelete() {
        assertFalse(storage.delete("1"));
    }

    @Test
    public void testDeleteReally() {
        var device = new RgbBulb("Bobo", 20, LocalDateTime.now());
        storage.store("1", device);
        assertTrue(storage.delete("1"));
    }
}
