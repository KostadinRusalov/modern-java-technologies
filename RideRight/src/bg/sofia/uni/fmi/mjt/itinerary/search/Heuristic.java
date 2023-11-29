package bg.sofia.uni.fmi.mjt.itinerary.search;

/**
 * Represent a heuristic function for informed search algorithms
 * (khm would be easier with lambdas...)
 *
 * @param <T> type of the object that
 * @param <V> type of the distance or cost
 */
public interface Heuristic<T, V> {
    /**
     *
     * @param from
     * @return the heuristic to from
     */
    V value(T from);
}
