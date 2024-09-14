package bg.sofia.uni.fmi.mjt.itinerary.norm;

/**
 *
 * @param <T> type of the object that implements Manhattan Distance
 * @param <D> type of distance (can be double, int, or something else)
 */
public interface ManhattanDistance<T, D> {
    /**
     *
     * @param other
     * @return return the distance to other
     */
    D distanceTo(T other);
}
