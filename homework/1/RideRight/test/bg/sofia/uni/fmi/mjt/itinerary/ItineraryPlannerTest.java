package bg.sofia.uni.fmi.mjt.itinerary;

import bg.sofia.uni.fmi.mjt.itinerary.exception.CityNotKnownException;
import bg.sofia.uni.fmi.mjt.itinerary.exception.NoPathToDestinationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.itinerary.vehicle.VehicleType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ItineraryPlannerTest {

    @Test
    public void testBullshitCases() {
        ItineraryPlanner planner = new RideRight(Collections.emptyList());

        assertThrows(CityNotKnownException.class,
            () -> planner.findCheapestPath(new City("A", new Location(-6, 2)),
                new City("B", new Location(4, 2)), false));

        assertThrows(CityNotKnownException.class,
            () -> planner.findCheapestPath(new City("A", new Location(-6, 2)),
                new City("B", new Location(4, 2)), true));
    }


    @Test
    public void testTransfer() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(800, -1000));
        City b = new City("B", new Location(90, -90));

        ItineraryPlanner planner = new RideRight(List.of(
            new Journey(BUS, a, b, BigDecimal.valueOf(100)),
            new Journey(TRAIN, a, b, BigDecimal.valueOf(1000)),
            new Journey(PLANE, a, b, BigDecimal.valueOf(69))
        ));

        assertEquals(List.of(new Journey(PLANE, a, b, BigDecimal.valueOf(69))),
            planner.findCheapestPath(a, b, true));

        assertEquals(List.of(new Journey(PLANE, a, b, BigDecimal.valueOf(69))),
            planner.findCheapestPath(a, b, false));

    }

    @Test
    public void testPythagoras() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(4990, 0));
        City b = new City("C", new Location(0, 0));
        City c = new City("B", new Location(3000, 0));

        ItineraryPlanner planner = new RideRight(List.of(
            new Journey(TRAIN, a, b, BigDecimal.valueOf(100)),
            new Journey(TRAIN, a, c, BigDecimal.valueOf(20)),
            new Journey(TRAIN, c, b, BigDecimal.valueOf(80))
        ));

        List<Journey> expected = List.of(new Journey(TRAIN, a, b, BigDecimal.valueOf(100)));

        assertEquals(expected, planner.findCheapestPath(a, b, false));

        assertEquals(expected, planner.findCheapestPath(a, b, true));
    }

    @Test
    public void testPythagorasAgain() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(4990, 0));
        City b = new City("C", new Location(0, 0));
        City c = new City("B", new Location(3000, 0));

        ItineraryPlanner planner = new RideRight(List.of(
            new Journey(TRAIN, a, b, BigDecimal.valueOf(100)),
            new Journey(TRAIN, a, c, BigDecimal.valueOf(20)),
            new Journey(TRAIN, c, b, BigDecimal.valueOf(70))
        ));


        assertEquals(List.of(new Journey(TRAIN, a, b, BigDecimal.valueOf(100))),
            planner.findCheapestPath(a, b, false));

        assertEquals(List.of(
                new Journey(TRAIN, a, c, BigDecimal.valueOf(20)),
                new Journey(TRAIN, c, b, BigDecimal.valueOf(70))
            ), planner.findCheapestPath(a, b, true)
        );
    }


    @Test
    public void testNoConnection() {
        City a = new City("A", new Location(-6, 2));
        City b = new City("B", new Location(4, 2));
        City c = new City("C", new Location(2, -4));

        City d = new City("D", new Location(-4, -4));
        City e = new City("E", new Location(6, -2));
        City f = new City("F", new Location(4, -8));

        List<Journey> journeys = List.of(
            new Journey(BUS, a, b, new BigDecimal("10")),
            new Journey(PLANE, c, b, new BigDecimal("60")),
            new Journey(TRAIN, a, c, new BigDecimal("20")),

            new Journey(PLANE, d, a, new BigDecimal("70")),
            new Journey(PLANE, d, c, new BigDecimal("50")),
            new Journey(TRAIN, d, f, new BigDecimal("25")),

            new Journey(TRAIN, f, e, new BigDecimal("15")),
            new Journey(TRAIN, e, c, new BigDecimal("15"))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(a, e, true));

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(e, a, true));

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(b, d, true));
    }

    @Test
    public void testNoCity() {
        City a = new City("A", new Location(-6, 2));
        City b = new City("B", new Location(4, 2));
        City c = new City("C", new Location(2, -4));

        List<Journey> journeys = List.of(
            new Journey(BUS, a, b, new BigDecimal("10")),
            new Journey(PLANE, b, a, new BigDecimal("60"))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertThrows(CityNotKnownException.class,
            () -> planner.findCheapestPath(a, c, true));
    }

    @Test
    public void testNoJourneys() {
        City a = new City("A", new Location(-6, 2));
        City b = new City("B", new Location(4, 2));
        City c = new City("C", new Location(2, -4));

        List<Journey> journeys = List.of(
            new Journey(BUS, a, b, new BigDecimal("10")),
            new Journey(PLANE, c, b, new BigDecimal("60"))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(b, a, true));

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(b, c, true));

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(a, c, true));
    }

    @Test
    public void testNames() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(-600, 200));
        City bbba = new City("Bbba", new Location(400, 2000));
        City bbbb = new City("Bbbb", new Location(4000, 200));
        City c = new City("C", new Location(200, -400));

        List<Journey> journeys = List.of(
            new Journey(PLANE, a, bbbb, BigDecimal.valueOf(100)),
            new Journey(PLANE, a, bbba, BigDecimal.valueOf(100)),
            new Journey(TRAIN, bbba, c, BigDecimal.valueOf(200)),
            new Journey(TRAIN, bbbb, c, BigDecimal.valueOf(300))
        );
        ItineraryPlanner planner = new RideRight(journeys);

        assertEquals(List.of(
                new Journey(PLANE, a, bbba, BigDecimal.valueOf(100)),
                new Journey(TRAIN, bbba, c, BigDecimal.valueOf(200))
            ),
            planner.findCheapestPath(a, c, true)
        );
    }

    @Test
    public void testTwoCitiesThreeJourneys() throws CityNotKnownException, NoPathToDestinationException {
        City sofia = new City("Sofia", new Location(0, 2000));
        City plovdiv = new City("Plovdiv", new Location(4000, 1000));

        List<Journey> schedule = List.of(
            new Journey(PLANE, sofia, plovdiv, new BigDecimal("90")),
            new Journey(BUS, sofia, plovdiv, new BigDecimal("20")),
            new Journey(TRAIN, sofia, plovdiv, new BigDecimal("20"))
        );
        ItineraryPlanner planner = new RideRight(schedule);

        assertEquals(List.of(new Journey(TRAIN, sofia, plovdiv, new BigDecimal("20"))),
            planner.findCheapestPath(sofia, plovdiv, false));
    }

    @Test
    public void test() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(-6, 2));
        City b = new City("B", new Location(4, 2));
        City c = new City("C", new Location(2, -4));
        City d = new City("D", new Location(-4, -4));
        City e = new City("E", new Location(6, -2));
        City f = new City("F", new Location(4, -8));
        City g = new City("G", new Location(10, -4));

        List<Journey> journeys = List.of(
            new Journey(BUS, a, b, new BigDecimal("10")),
            new Journey(PLANE, a, b, new BigDecimal("60")),
            new Journey(TRAIN, a, d, new BigDecimal("40")),

            new Journey(BUS, b, e, new BigDecimal("10")),
            new Journey(BUS, b, g, new BigDecimal("40")),
            new Journey(PLANE, b, g, new BigDecimal("60")),

            new Journey(TRAIN, c, a, new BigDecimal("10")),
            new Journey(BUS, c, e, new BigDecimal("35")),

            new Journey(PLANE, d, a, new BigDecimal("70")),
            new Journey(PLANE, d, c, new BigDecimal("50")),

            new Journey(TRAIN, e, f, new BigDecimal("25")),

            new Journey(TRAIN, f, d, new BigDecimal("15")),
            new Journey(BUS, f, d, new BigDecimal("20"))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertEquals(List.of(
                new Journey(BUS, a, b, new BigDecimal("10")),
                new Journey(BUS, b, g, new BigDecimal("40"))
            ),
            planner.findCheapestPath(a, g, true)
        );

        assertEquals(List.of(
                new Journey(BUS, a, b, new BigDecimal("10")),
                new Journey(BUS, b, e, new BigDecimal("10")),
                new Journey(TRAIN, e, f, new BigDecimal("25"))
            ),
            planner.findCheapestPath(a, f, true)
        );

        assertThrows(NoPathToDestinationException.class,
            () -> planner.findCheapestPath(g, f, true));

        assertEquals(List.of(
                new Journey(TRAIN, c, a, new BigDecimal("10")),
                new Journey(BUS, a, b, new BigDecimal("10")),
                new Journey(BUS, b, e, new BigDecimal("10")),
                new Journey(TRAIN, e, f, new BigDecimal("25"))
            ),
            planner.findCheapestPath(c, f, true)
        );

    }

    @Test
    public void testFromTask() throws CityNotKnownException, NoPathToDestinationException {
        City sofia = new City("Sofia", new Location(0, 2000));
        City plovdiv = new City("Plovdiv", new Location(4000, 1000));
        City varna = new City("Varna", new Location(9000, 3000));
        City burgas = new City("Burgas", new Location(9000, 1000));
        City ruse = new City("Ruse", new Location(7000, 4000));
        City blagoevgrad = new City("Blagoevgrad", new Location(0, 1000));
        City kardzhali = new City("Kardzhali", new Location(3000, 0));
        City tarnovo = new City("Tarnovo", new Location(5000, 3000));

        List<Journey> schedule = List.of(
            new Journey(BUS, sofia, blagoevgrad, new BigDecimal("20")),
            new Journey(BUS, blagoevgrad, sofia, new BigDecimal("20")),
            new Journey(BUS, sofia, plovdiv, new BigDecimal("90")),
            new Journey(BUS, plovdiv, sofia, new BigDecimal("90")),
            new Journey(BUS, plovdiv, kardzhali, new BigDecimal("50")),
            new Journey(BUS, kardzhali, plovdiv, new BigDecimal("50")),
            new Journey(BUS, plovdiv, burgas, new BigDecimal("90")),
            new Journey(BUS, burgas, plovdiv, new BigDecimal("90")),
            new Journey(BUS, burgas, varna, new BigDecimal("60")),
            new Journey(BUS, varna, burgas, new BigDecimal("60")),
            new Journey(BUS, sofia, tarnovo, new BigDecimal("150")),
            new Journey(BUS, tarnovo, sofia, new BigDecimal("150")),
            new Journey(BUS, plovdiv, tarnovo, new BigDecimal("40")),
            new Journey(BUS, tarnovo, plovdiv, new BigDecimal("40")),
            new Journey(BUS, tarnovo, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, tarnovo, new BigDecimal("70")),
            new Journey(BUS, varna, ruse, new BigDecimal("70")),
            new Journey(BUS, ruse, varna, new BigDecimal("70")),
            new Journey(PLANE, varna, burgas, new BigDecimal("200")),
            new Journey(PLANE, burgas, varna, new BigDecimal("200")),
            new Journey(PLANE, burgas, sofia, new BigDecimal("150")),
            new Journey(PLANE, sofia, burgas, new BigDecimal("250")),
            new Journey(PLANE, varna, sofia, new BigDecimal("290")),
            new Journey(PLANE, sofia, varna, new BigDecimal("300"))
        );

        RideRight rideRight = new RideRight(schedule);

        assertEquals(List.of(
                new Journey(BUS, varna, burgas, new BigDecimal("60")),
                new Journey(BUS, burgas, plovdiv, new BigDecimal("90")),
                new Journey(BUS, plovdiv, kardzhali, new BigDecimal("50"))
            ),
            rideRight.findCheapestPath(varna, kardzhali, true)
        );

        assertThrows(NoPathToDestinationException.class,
            () -> rideRight.findCheapestPath(varna, kardzhali, false));

        assertEquals(List.of(new Journey(BUS, varna, burgas, new BigDecimal("60"))),
            rideRight.findCheapestPath(varna, burgas, false));

        assertEquals(List.of(
                new Journey(BUS, ruse, tarnovo, new BigDecimal("70")),
                new Journey(BUS, tarnovo, plovdiv, new BigDecimal("40")),
                new Journey(BUS, plovdiv, sofia, new BigDecimal("90")),
                new Journey(BUS, sofia, blagoevgrad, new BigDecimal("20"))
            ),
            rideRight.findCheapestPath(ruse, blagoevgrad, true)
        );
    }

    @Test
    public void testFromYouTube() throws CityNotKnownException, NoPathToDestinationException {
        City s = new City("S", new Location(2000, 500));
        City a = new City("A", new Location(3500, 0));
        City b = new City("B", new Location(750, 750));
        City c = new City("C", new Location(500, 1500));
        City d = new City("D", new Location(2000, -1000));
        City e = new City("E", new Location(-1000, 1500));
        City f = new City("F", new Location(1500, -1500));
        City g1 = new City("G1", new Location(0, 0));
        City g2 = new City("G2", new Location(0, 0));
        City g3 = new City("G3", new Location(0, 0));

        List<Journey> journeys = List.of(
            new Journey(BUS, s, a, BigDecimal.valueOf(50)),
            new Journey(PLANE, s, b, BigDecimal.valueOf(90)),
            new Journey(PLANE, s, d, BigDecimal.valueOf(60)),

            new Journey(BUS, a, b, BigDecimal.valueOf(30)),
            new Journey(PLANE, a, g1, BigDecimal.valueOf(90)),

            new Journey(BUS, b, a, BigDecimal.valueOf(20)),
            new Journey(TRAIN, b, c, BigDecimal.valueOf(10)),

            new Journey(BUS, c, s, BigDecimal.valueOf(60)),
            new Journey(TRAIN, c, g2, BigDecimal.valueOf(50)),
            new Journey(PLANE, c, f, BigDecimal.valueOf(70)),

            new Journey(TRAIN, d, s, BigDecimal.valueOf(10)),
            new Journey(BUS, d, c, BigDecimal.valueOf(20)),
            new Journey(BUS, d, e, BigDecimal.valueOf(20)),

            new Journey(PLANE, e, g3, BigDecimal.valueOf(70)),

            new Journey(TRAIN, f, d, BigDecimal.valueOf(20)),
            new Journey(PLANE, f, g3, BigDecimal.valueOf(80))
        );

        ItineraryPlanner planner = new RideRight(journeys);
        assertEquals(List.of(
                new Journey(PLANE, s, d, BigDecimal.valueOf(60)),
                new Journey(BUS, d, c, BigDecimal.valueOf(20)),
                new Journey(TRAIN, c, g2, BigDecimal.valueOf(50))
            ),
            planner.findCheapestPath(s, g2, true)
        );
    }

    @Test
    public void testFromYouTube2() throws CityNotKnownException, NoPathToDestinationException {
        City s = new City("S", new Location(0, 5000));
        City a = new City("A", new Location(4000, 500));
        City b = new City("B", new Location(3000, 500));
        City c = new City("C", new Location(-2000, 2000));
        City d = new City("D", new Location(2000, -2000));
        City e = new City("E", new Location(0, 0));
        City f = new City("F", new Location(2000, 1000));
        City g = new City("G", new Location(-1000, 500));
        City h = new City("H", new Location(-1500, -1500));
        City i = new City("I", new Location(-2000, 0));
        City j = new City("J", new Location(1500, -500));
        City k = new City("K", new Location(1000, 500));
        City l = new City("L", new Location(1000, 2000));

        List<Journey> journeys = List.of(
            new Journey(PLANE, s, a, BigDecimal.valueOf(70)),
            new Journey(BUS, s, b, BigDecimal.valueOf(20)),
            new Journey(BUS, s, c, BigDecimal.valueOf(30)),

            new Journey(TRAIN, a, s, BigDecimal.valueOf(90)),
            new Journey(TRAIN, a, b, BigDecimal.valueOf(30)),
            new Journey(BUS, a, d, BigDecimal.valueOf(40)),

            new Journey(TRAIN, b, s, BigDecimal.valueOf(22)),
            new Journey(PLANE, b, a, BigDecimal.valueOf(25)),
            new Journey(BUS, b, d, BigDecimal.valueOf(40)),
            new Journey(TRAIN, b, h, BigDecimal.valueOf(10)),

            new Journey(PLANE, c, s, BigDecimal.valueOf(25)),
            new Journey(BUS, c, l, BigDecimal.valueOf(20)),

            new Journey(PLANE, d, a, BigDecimal.valueOf(35)),
            new Journey(TRAIN, d, b, BigDecimal.valueOf(50)),
            new Journey(PLANE, d, f, BigDecimal.valueOf(50)),

            new Journey(TRAIN, e, g, BigDecimal.valueOf(30)),
            new Journey(PLANE, e, k, BigDecimal.valueOf(50)),

            new Journey(TRAIN, f, d, BigDecimal.valueOf(60)),
            new Journey(TRAIN, f, h, BigDecimal.valueOf(30)),

            new Journey(BUS, g, h, BigDecimal.valueOf(20)),
            new Journey(BUS, g, e, BigDecimal.valueOf(20)),

            new Journey(TRAIN, h, b, BigDecimal.valueOf(10)),
            new Journey(PLANE, h, g, BigDecimal.valueOf(15)),

            new Journey(TRAIN, i, l, BigDecimal.valueOf(40)),
            new Journey(PLANE, i, j, BigDecimal.valueOf(60)),
            new Journey(PLANE, i, k, BigDecimal.valueOf(50)),

            new Journey(BUS, j, l, BigDecimal.valueOf(40)),
            new Journey(TRAIN, j, i, BigDecimal.valueOf(60)),
            new Journey(BUS, j, k, BigDecimal.valueOf(40)),

            new Journey(BUS, k, i, BigDecimal.valueOf(40)),
            new Journey(TRAIN, k, j, BigDecimal.valueOf(50)),
            new Journey(PLANE, k, e, BigDecimal.valueOf(50)),

            new Journey(BUS, l, c, BigDecimal.valueOf(20)),
            new Journey(TRAIN, l, j, BigDecimal.valueOf(50)),
            new Journey(BUS, l, i, BigDecimal.valueOf(40))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertEquals(List.of(
                new Journey(BUS, s, b, BigDecimal.valueOf(20)),
                new Journey(TRAIN, b, h, BigDecimal.valueOf(10)),
                new Journey(PLANE, h, g, BigDecimal.valueOf(15)),
                new Journey(BUS, g, e, BigDecimal.valueOf(20))
            ),
            planner.findCheapestPath(s, e, true)
        );
    }

    @Test
    public void testFromSomeArticle() throws CityNotKnownException, NoPathToDestinationException {
        City a = new City("A", new Location(10000, 2750));
        City b = new City("B", new Location(-3000, -8950));
        City c = new City("C", new Location(-6650, 0));
        City d = new City("D", new Location(6000, -550));
        City h = new City("H", new Location(-3100, -3100));
        City i = new City("I", new Location(-5025, 5025));
        City m = new City("M", new Location(1900, 1000));
        City n = new City("N", new Location(0, 0));
        City r = new City("R", new Location(0, 2650));
        City s = new City("S", new Location(-4150, 2150));

        List<Journey> journeys = List.of(
            new Journey(BUS, a, b, BigDecimal.valueOf(70)),
            new Journey(TRAIN, a, c, BigDecimal.valueOf(160)),
            new Journey(PLANE, a, c, BigDecimal.valueOf(125)),

            new Journey(BUS, b, i, BigDecimal.valueOf(70)),

            new Journey(BUS, c, a, BigDecimal.valueOf(135)),
            new Journey(TRAIN, c, d, BigDecimal.valueOf(90)),
            new Journey(TRAIN, c, i, BigDecimal.valueOf(140)),
            new Journey(BUS, c, i, BigDecimal.valueOf(130)),
            new Journey(PLANE, c, h, BigDecimal.valueOf(65)),

            new Journey(BUS, d, c, BigDecimal.valueOf(70)),
            new Journey(PLANE, d, n, BigDecimal.valueOf(215)),
            new Journey(BUS, d, n, BigDecimal.valueOf(300)),

            new Journey(PLANE, h, m, BigDecimal.valueOf(125)),
            new Journey(BUS, h, m, BigDecimal.valueOf(140)),
            new Journey(PLANE, h, s, BigDecimal.valueOf(130)),
            new Journey(TRAIN, h, s, BigDecimal.valueOf(150)),

            new Journey(BUS, i, b, BigDecimal.valueOf(80)),
            new Journey(TRAIN, i, c, BigDecimal.valueOf(130)),

            new Journey(TRAIN, m, n, BigDecimal.valueOf(65)),

            new Journey(BUS, n, d, BigDecimal.valueOf(200)),
            new Journey(TRAIN, n, m, BigDecimal.valueOf(70)),
            new Journey(PLANE, n, m, BigDecimal.valueOf(50)),
            new Journey(BUS, n, r, BigDecimal.valueOf(70)),

            new Journey(BUS, r, n, BigDecimal.valueOf(70)),

            new Journey(PLANE, s, h, BigDecimal.valueOf(160)),
            new Journey(TRAIN, s, m, BigDecimal.valueOf(130)),
            new Journey(BUS, s, m, BigDecimal.valueOf(120))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertEquals(List.of(
                new Journey(PLANE, a, c, BigDecimal.valueOf(125)),
                new Journey(PLANE, c, h, BigDecimal.valueOf(65)),
                new Journey(BUS, h, m, BigDecimal.valueOf(140)),
                new Journey(TRAIN, m, n, BigDecimal.valueOf(65))
            ),
            planner.findCheapestPath(a, n, true)
        );
    }

    @Test
    public void testFromMisho() throws CityNotKnownException, NoPathToDestinationException {
        City Köln = new City("Köln", new Location(10000, 1900));
        City Metz = new City("Metz", new Location(-100, -8000));
        City Saarbrücken = new City("Saarbrücken", new Location(-5000, 250));
        City Koblenz = new City("Koblenz", new Location(6000, -2000));
        City Kaiserslautern = new City("Kaiserslautern", new Location(-3100, -200));
        City Frankfurt = new City("Frankfurt", new Location(-6250, 0));
        City Ludwigshafen = new City("Ludwigshafen", new Location(1900, 750));
        City Karlsruhe = new City("Karlsruhe", new Location(0, 0));
        City Würzburg = new City("Würzburg", new Location(4350, 2650));

        List<Journey> journeys = List.of(
            new Journey(BUS, Köln, Metz, BigDecimal.valueOf(290)),
            new Journey(BUS, Köln, Frankfurt, BigDecimal.valueOf(190)),
            new Journey(BUS, Köln, Saarbrücken, BigDecimal.valueOf(260)),

            new Journey(BUS, Metz, Köln, BigDecimal.valueOf(260)),
            new Journey(BUS, Metz, Saarbrücken, BigDecimal.valueOf(260)),

            new Journey(BUS, Saarbrücken, Köln, BigDecimal.valueOf(260)),
            new Journey(BUS, Saarbrücken, Koblenz, BigDecimal.valueOf(200)),
            new Journey(BUS, Saarbrücken, Metz, BigDecimal.valueOf(68)),
            new Journey(BUS, Saarbrücken, Kaiserslautern, BigDecimal.valueOf(66)),

            new Journey(BUS, Koblenz, Saarbrücken, BigDecimal.valueOf(200)),
            new Journey(BUS, Koblenz, Frankfurt, BigDecimal.valueOf(126)),

            new Journey(BUS, Kaiserslautern, Saarbrücken, BigDecimal.valueOf(70)),
            new Journey(BUS, Kaiserslautern, Frankfurt, BigDecimal.valueOf(110)),
            new Journey(BUS, Kaiserslautern, Ludwigshafen, BigDecimal.valueOf(53)),

            new Journey(BUS, Frankfurt, Köln, BigDecimal.valueOf(190)),
            new Journey(BUS, Frankfurt, Koblenz, BigDecimal.valueOf(126)),
            new Journey(BUS, Frankfurt, Kaiserslautern, BigDecimal.valueOf(110)),
            new Journey(BUS, Frankfurt, Würzburg, BigDecimal.valueOf(116)),

            new Journey(BUS, Ludwigshafen, Kaiserslautern, BigDecimal.valueOf(53)),
            new Journey(BUS, Ludwigshafen, Würzburg, BigDecimal.valueOf(183)),
            new Journey(BUS, Ludwigshafen, Karlsruhe, BigDecimal.valueOf(77)),

            new Journey(BUS, Würzburg, Frankfurt, BigDecimal.valueOf(116)),
            new Journey(BUS, Würzburg, Ludwigshafen, BigDecimal.valueOf(183)),
            new Journey(BUS, Würzburg, Karlsruhe, BigDecimal.valueOf(193)),

            new Journey(BUS, Karlsruhe, Ludwigshafen, BigDecimal.valueOf(53)),
            new Journey(BUS, Karlsruhe, Würzburg, BigDecimal.valueOf(193))
        );

        ItineraryPlanner planner = new RideRight(journeys);

        assertEquals(List.of(
                new Journey(BUS, Köln, Frankfurt, BigDecimal.valueOf(190)),
                new Journey(BUS, Frankfurt, Kaiserslautern, BigDecimal.valueOf(110)),
                new Journey(BUS, Kaiserslautern, Ludwigshafen, BigDecimal.valueOf(53)),
                new Journey(BUS, Ludwigshafen, Karlsruhe, BigDecimal.valueOf(77))
            ),
            planner.findCheapestPath(Köln, Karlsruhe, true)
        );
    }
}
