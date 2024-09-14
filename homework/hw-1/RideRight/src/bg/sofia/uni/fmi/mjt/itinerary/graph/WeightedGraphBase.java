package bg.sofia.uni.fmi.mjt.itinerary.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * Abstract base class for a weighted multi graph
 *
 * @param <V> type of the vertices
 * @param <E> type of the edges
 * @param <T> type of the weight
 *
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.MultiGraph
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraph
 *
 */
public abstract class WeightedGraphBase<V, E extends WeightedEdge<V, T>, T>
    implements WeightedGraph<V, E, T> {

    private Map<V, Set<E>> adjacencyMap;

    public WeightedGraphBase(Map<V, Set<E>> adjacencyMap) {
        this.adjacencyMap = adjacencyMap;
    }

    public WeightedGraphBase(Collection<E> edges) {
        this.adjacencyMap = adjacencyMapFrom(edges);
    }

    @Override
    public boolean hasVertex(V vertex) {
        return adjacencyMap.containsKey(vertex);
    }

    @Override
    public Set<E> getEdges(V from, V to) {
        Set<E> edges = new HashSet<>();

        for (E edge : getAllEdges(from)) {
            if (edge.to().equals(to)) {
                edges.add(edge);
            }
        }

        return edges;
    }

    @Override
    public Set<E> getAllEdges(V from) {
        return adjacencyMap.get(from);
    }

    private Map<V, Set<E>> adjacencyMapFrom(Collection<E> edges) {
        Map<V, Set<E>> adjacencyMap = new HashMap<>();

        for (E edge : edges) {
            adjacencyMap.putIfAbsent(edge.from(), new HashSet<>());
            adjacencyMap.putIfAbsent(edge.to(), new HashSet<>());
            adjacencyMap.get(edge.from()).add(edge);
        }

        return adjacencyMap;
    }
}
