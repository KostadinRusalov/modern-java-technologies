package bg.sofia.uni.fmi.mjt.itinerary.search;

import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.SequencedCollection;

/**
 * Implements the A* searching algorithm for a generic A* scorable graph (a little bit overkill, but why not ;))
 */
public class AStar {

    /**
     * Behold, the A* searching algorithm
     *
     * @param graph     the graph on which the algorithm is run
     * @param start     the starting vertex
     * @param goal      the target vertex
     * @param heuristic the heuristic function for the algorithm (will refactor with lambdas in the future)
     * @param <G>       an A* scorable graph
     * @param <V>       type of the vertices
     * @param <E>       type of the edge
     * @param <T>       type of the weight of edge
     * @return a sequenced collection of the edges from start to goal
     * @see <a href="https://youtu.be/6TsL96NAZCo?si=bt_8Ospcg-PgFMqb">Learned from: A* Search</a>
     *
     */
    public static <G extends AStarScorableGraph<V, E, T>, V extends Comparable<V>,
        E extends WeightedEdge<V, T>, T extends Comparable<T>>
        SequencedCollection<E> search(G graph, V start, V goal, Heuristic<V, T> heuristic) {

        Map<V, T> visited = new HashMap<>();
        Queue<SearchRecord<V, E, T>> expand = new PriorityQueue<>();
        SearchRecord<V, E, T> current = new SearchRecord<>(start, null, heuristic.value(start), null);

        while (current != null && current.vertex() != goal) {
            if (visited.containsKey(current.vertex()) && visited.get(current.vertex()).compareTo(current.score()) < 0) {
                current = expand.poll();
                continue;
            }
            for (var edge : graph.getAllEdges(current.vertex())) {
                T score = graph.calculateScore(current.score(), edge, heuristic);
                SearchRecord<V, E, T> open = new SearchRecord<>(edge.to(), edge, score, current);

                if (visited.containsKey(edge.to()) && visited.get(edge.to()).compareTo(score) <= 0) {
                    continue;
                }

                visited.remove(edge.to());
                expand.add(open);
            }

            visited.put(current.vertex(), current.score());
            current = expand.poll();
        }

        return traceBack(current);
    }

    /**
     * @param searchRecord the end of the path
     * @return sequenced collection of the edges in the path
     */
    private static <V extends Comparable<V>, E extends WeightedEdge<V, T>, T extends Comparable<T>>
        SequencedCollection<E> traceBack(SearchRecord<V, E, T> searchRecord) {
        SequencedCollection<E> path = new ArrayList<>();
        while (searchRecord != null && searchRecord.edge() != null) {
            path.add(searchRecord.edge());
            searchRecord = searchRecord.previous();
        }
        return path.reversed();
    }

    /**
     * Used for internal implementation of the A* algorithm (I wanted to be exotic at least once and use nested classes)
     *
     * @param vertex
     * @param edge
     * @param score    the A* star score for the current vertex
     * @param previous used to track the path
     * @param <V>      type of the vertex
     * @param <E>      type of the edge
     * @param <T>      type of the weight of the edge
     */
    private record SearchRecord<V extends Comparable<V>, E extends WeightedEdge<V, T>, T extends Comparable<T>>(
        V vertex, E edge, T score, SearchRecord<V, E, T> previous)
        implements Comparable<SearchRecord<V, E, T>> {

        @Override
        public int compareTo(SearchRecord<V, E, T> other) {
            int compared = score.compareTo(other.score);
            if (compared == 0) {
                return vertex.compareTo(other.vertex);
            }
            return compared;
        }
    }
}

