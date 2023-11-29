package bg.sofia.uni.fmi.mjt.itinerary.graph;

/**
 * Represents the edge of a graph.
 *
 * @param <V> type of the vertices.
 */
public interface Edge<V> {
    /**
     *
     * @return the beginning of the edge.
     */
    V from();

    /**
     *
     * @return the end of the edge.
     */
    V to();
}
