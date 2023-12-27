package bg.sofia.uni.fmi.mjt.space.rocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;
import java.util.Optional;

public class RocketTest {

    @Test
    public void testRocketFromStringWithFullData() {
        String line = "35,Antares 230+,https://en.wikipedia.org/wiki/Antares_%28rocket%29#Antares_230+,42.5 m";
        Rocket expected = new Rocket("35", "Antares 230+",
            Optional.of("https://en.wikipedia.org/wiki/Antares_%28rocket%29#Antares_230+"), Optional.of(42.5));

        assertEquals(expected, Rocket.from(line));
    }

    @Test
    public void testRocketFromStringWithoutWiki() {
        String line = "35,Antares 230+,,42.5 m";
        Rocket expected = new Rocket("35", "Antares 230+", Optional.empty(), Optional.of(42.5));

        assertEquals(expected, Rocket.from(line));
    }

    @Test
    public void testRocketFromStringWithoutHeight() {
        String line = "35,Antares 230+,https://en.wikipedia.org/wiki/Antares_%28rocket%29#Antares_230+,";
        Rocket expected = new Rocket("35", "Antares 230+",
            Optional.of("https://en.wikipedia.org/wiki/Antares_%28rocket%29#Antares_230+"),
            Optional.empty());

        assertEquals(expected, Rocket.from(line));
    }

    @Test
    public void testRocketFromStringWithoutWikiAndHeight() {
        String line = "35,Antares 230+,,";
        Rocket expected = new Rocket("35", "Antares 230+", Optional.empty(), Optional.empty());

        assertEquals(expected, Rocket.from(line));
    }

    @Test
    public void testRocketFromStringWithCommaInFields() {
        String line = "150,\"Delta IV Medium+ (5,4)\",https://en.wikipedia.org/wiki/Delta_IV,66.4 m";
        Rocket expected = new Rocket("150", "Delta IV Medium+ (5,4)",
            Optional.of("https://en.wikipedia.org/wiki/Delta_IV"), Optional.of(66.4));

        assertEquals(expected, Rocket.from(line));
    }

    @Test
    public void testReadCSV() {
        String csv = """
            "",Name,Wiki,Rocket Height
            190,H-IIS,https://en.wikipedia.org/wiki/H-II,49.0 m
            191,H-I UM-129A (6SO),https://en.wikipedia.org/wiki/H-I,42.0 m
            192,H-I UM-129A (9SO),,42.0 m
            193,Hyperbola-1,https://en.wikipedia.org/wiki/I-Space_(Chinese_company)#Hyperbola-1,21.0 m
            194,Jielong-1,,
            195,Juno I,https://en.wikipedia.org/wiki/Juno_I,21.2 m
            196,Juno II,https://en.wikipedia.org/wiki/Juno_II,24.0 m
            197,Kaituozhe 1,https://en.wikipedia.org/wiki/Kaituozhe_(rocket_family),
            198,Kaituozhe 2,https://en.wikipedia.org/wiki/Kaituozhe_(rocket_family)#KT-2,
            199,Kuaizhou 1,https://en.wikipedia.org/wiki/Kuaizhou,
            200,Kuaizhou 11,,25.0 m
            """;

        List<Rocket> expected = List.of(
            new Rocket("190", "H-IIS", Optional.of("https://en.wikipedia.org/wiki/H-II"), Optional.of(49.0)),
            new Rocket("191", "H-I UM-129A (6SO)", Optional.of("https://en.wikipedia.org/wiki/H-I"), Optional.of(42.0)),
            new Rocket("192", "H-I UM-129A (9SO)", Optional.empty(), Optional.of(42.0)),
            new Rocket("193", "Hyperbola-1",
                Optional.of("https://en.wikipedia.org/wiki/I-Space_(Chinese_company)#Hyperbola-1"), Optional.of(21.0)),
            new Rocket("194", "Jielong-1", Optional.empty(), Optional.empty()),
            new Rocket("195", "Juno I", Optional.of("https://en.wikipedia.org/wiki/Juno_I"), Optional.of(21.2)),
            new Rocket("196", "Juno II", Optional.of("https://en.wikipedia.org/wiki/Juno_II"), Optional.of(24.0)),
            new Rocket("197", "Kaituozhe 1", Optional.of("https://en.wikipedia.org/wiki/Kaituozhe_(rocket_family)"),
                Optional.empty()),
            new Rocket("198", "Kaituozhe 2",
                Optional.of("https://en.wikipedia.org/wiki/Kaituozhe_(rocket_family)#KT-2"), Optional.empty()),
            new Rocket("199", "Kuaizhou 1", Optional.of("https://en.wikipedia.org/wiki/Kuaizhou"), Optional.empty()),
            new Rocket("200", "Kuaizhou 11", Optional.empty(), Optional.of(25.0))
        );

        assertEquals(expected, Rocket.readCSV(new StringReader(csv)));
    }
}
