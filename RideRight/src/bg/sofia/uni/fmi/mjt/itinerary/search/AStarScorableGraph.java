package bg.sofia.uni.fmi.mjt.itinerary.search;

import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedEdge;
import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraph;

/**
 * (Well, I wanted to make the A* searching algorithm as generic as possible, but the score is calculated differently
 * For instance, with ints and doubles I can use +, -, but with BigDecimal .multiply(), .add() must be used)
 *
 * @inheritDoc
 * @see bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraph
 * @see bg.sofia.uni.fmi.mjt.itinerary.search.Heuristic
 */
public interface AStarScorableGraph<V, E extends WeightedEdge<V, T>, T> extends WeightedGraph<V, E, T> {
    /**
     *
     * @param currentScore
     * @param edge
     * @param heuristic
     * @return the A* score, which is: currentScore - heuristic(edge.from()) + edge.weight() + heuristic(edge.to())
     */
    T calculateScore(T currentScore, E edge, Heuristic<V, T> heuristic);
}
