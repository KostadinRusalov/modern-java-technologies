package bg.sofia.uni.fmi.mjt.itinerary.graph;

import java.util.Set;

/**
 * Represents a multi graph.
 * <p>
 * !DISCLAIMER there should be more functions here I will add them in the future, so I can reuse the interface :))
 * </p>
 * @param <V> type of the vertices.
 * @param <E> type of the edges, implements the {@link bg.sofia.uni.fmi.mjt.itinerary.graph.Edge} interface
 */
public interface MultiGraph<V, E extends Edge<V>> {
    /**
     *
     * @param vertex
     * @return whether the graph has this vertex.
     */
    boolean hasVertex(V vertex);

    /**
     *
     * @param from
     * @param to
     * @return set of edges starting with from and ending in to.
     */
    Set<E> getEdges(V from, V to);

    /**
     *
     * @param from
     * @return set of all edges starting with from.
     */
    Set<E> getAllEdges(V from);
}
