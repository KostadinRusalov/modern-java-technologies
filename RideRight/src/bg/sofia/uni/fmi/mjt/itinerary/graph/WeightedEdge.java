package bg.sofia.uni.fmi.mjt.itinerary.graph;

/**
 * Represent the edge in a weighted graph.
 *
 * @inheritDoc
 * @param <T> the type of the weight
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.Edge
 */
public interface WeightedEdge<V, T> extends Edge<V> {
    /**
     *
     * @return the weight of the current edge.
     */
    T weight();
}
