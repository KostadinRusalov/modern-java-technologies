package bg.sofia.uni.fmi.mjt.itinerary.search;

import bg.sofia.uni.fmi.mjt.itinerary.norm.ManhattanDistance;

import java.math.BigDecimal;

/**
 * The heuristic function for the given task.
 *
 * @see bg.sofia.uni.fmi.mjt.itinerary.search.Heuristic
 * @see bg.sofia.uni.fmi.mjt.itinerary.norm.ManhattanDistance
 *
 * @param goal the target of the informed search
 * @param <T> the type of objects, must implement Manhattan distance
 */
public record ManhattanHeuristic<T extends ManhattanDistance<T, BigDecimal>>(T goal)
    implements Heuristic<T, BigDecimal> {
    private static final BigDecimal AVERAGE_COST = BigDecimal.valueOf(20);

    /**
     *
     * @param from
     * @return returns the cost from goal to other
     */
    @Override
    public BigDecimal value(T from) {
        return from.distanceTo(goal).multiply(AVERAGE_COST);
    }
}
