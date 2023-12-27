package bg.sofia.uni.fmi.mjt.space.rocket;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RocketStatusTest {

    @Test
    public void testRocketStatusFromInvalidString() {
        assertThrows(IllegalArgumentException.class, () -> RocketStatus.from("StatusInvalid"),
            "Rocket status should not be parsed when it is invalid");
    }
}
