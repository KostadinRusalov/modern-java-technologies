package bg.sofia.uni.fmi.mjt.itinerary.graph;

/**
 * Represent a weighted graph.
 *
 * @inheritDoc
 * @param <T> type of the weight
 *
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.MultiGraph
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedEdge
 */
public interface WeightedGraph<V, E extends WeightedEdge<V, T>, T> extends MultiGraph<V, E> {
}
