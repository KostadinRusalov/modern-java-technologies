package bg.sofia.uni.fmi.mjt.itinerary.search;

import bg.sofia.uni.fmi.mjt.itinerary.City;
import bg.sofia.uni.fmi.mjt.itinerary.Journey;
import bg.sofia.uni.fmi.mjt.itinerary.graph.WeightedGraphBase;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class SearchSpace
    extends WeightedGraphBase<City, Journey, BigDecimal>
    implements AStarScorableGraph<City, Journey, BigDecimal> {

    public SearchSpace(Collection<Journey> journeys) {
        super(journeys);
    }

    /** one-liner with streams and lambdas :(
     * <p>
     *   P.S. before Velev asks "Why do you know about lambdas before we have studied them?"
     *  I watched some of the YouTube lectures in the summer.
     * </p>
     * @param from
     * @param to
     * @return the cheapest journey
     */
    public Journey getCheapestJourney(City from, City to) {
        Set<Journey> journeys = getEdges(from, to);
        if (journeys.isEmpty()) {
            return null;
        }

        Journey cheapest = null;
        BigDecimal price = BigDecimal.valueOf(Long.MAX_VALUE);
        for (Journey journey : journeys) {
            if (journey.weight().compareTo(price) < 0) {
                price = journey.weight();
                cheapest = journey;
            }
        }

        return cheapest;
    }

    @Override
    public BigDecimal calculateScore(BigDecimal currentScore, Journey edge, Heuristic<City, BigDecimal> heuristic) {
        return currentScore
            .subtract(heuristic.value(edge.from()))
            .add(edge.weight())
            .add(heuristic.value(edge.to()));
    }
}
