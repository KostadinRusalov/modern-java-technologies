package bg.sofia.uni.fmi.mjt.trading.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathHelper {
    public static double round(double num, int places) {
        return new BigDecimal(Double.toString(num))
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
