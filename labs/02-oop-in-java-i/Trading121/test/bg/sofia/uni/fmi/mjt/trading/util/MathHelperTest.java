package bg.sofia.uni.fmi.mjt.trading.util;


import bg.sofia.uni.fmi.mjt.trading.util.MathHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathHelperTest {

    @Test
    void testRoundingHalfUp() {
        assertEquals(6.90, MathHelper.round(6.896, 2));
    }

    @Test
    void testRoundingHalfDown() {
        assertEquals(6.90, MathHelper.round(6.904, 2));
    }

    @Test
    void testRoundStaysTheSame() {
        assertEquals(6.9, MathHelper.round(6.9, 2));
    }
}
