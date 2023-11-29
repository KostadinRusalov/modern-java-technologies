package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.norm.ManhattanDistance;

import java.math.BigDecimal;

public record Location(int x, int y) implements ManhattanDistance<Location, BigDecimal> {
    private static final BigDecimal METERS_TO_KILOMETERS = BigDecimal.valueOf(1e-3);

    /**
     *
     * @param other
     * @return the distance to other in kilometers
     */
    @Override
    public BigDecimal distanceTo(Location other) {
        return new BigDecimal(Math.abs(other.x - this.x) + Math.abs(other.y - this.y))
            .multiply(METERS_TO_KILOMETERS);
    }
}