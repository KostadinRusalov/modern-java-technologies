package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import bg.sofia.uni.fmi.mjt.itinerary.search.AStar;
import bg.sofia.uni.fmi.mjt.itinerary.search.ManhattanHeuristic;
import bg.sofia.uni.fmi.mjt.itinerary.search.SearchSpace;

import java.util.List;
import java.util.SequencedCollection;

public class RideRight implements ItineraryPlanner {
    private final SearchSpace searchSpace;

    public RideRight(List<Journey> schedule) {
        searchSpace = new SearchSpace(schedule);
    }

    @Override
    public SequencedCollection<Journey> findCheapestPath(City start, City destination, boolean allowTransfer)
        throws CityNotKnownException, NoPathToDestinationException {
        if (!searchSpace.hasVertex(start)) {
            throw new CityNotKnownException("City %s is not known".formatted(start.toString()));
        }

        if (!searchSpace.hasVertex(destination)) {
            throw new CityNotKnownException("City %s is not known".formatted(destination.toString()));
        }

        if (!allowTransfer) {
            Journey journey = searchSpace.getCheapestJourney(start, destination);
            if (journey == null) {
                throw new NoPathToDestinationException("There is no path between %s and %s"
                    .formatted(start.toString(), destination.toString()));
            }
            return List.of(journey);
        }

        // the heuristic could have been a lambda... :(
        var path = AStar.search(searchSpace, start, destination, new ManhattanHeuristic<>(destination));
        if (path.isEmpty()) {
            throw new NoPathToDestinationException("There is no path between %s and %s"
                .formatted(start.toString(), destination.toString()));
        }

        return path;
    }
}
