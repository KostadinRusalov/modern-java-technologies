package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissionTest {

    private static Mission mission;

    @BeforeAll
    public static void setUp() {
        mission = new Mission("494", "MHI", "LA-Y2, Tanegashima Space Center, Japan",
            LocalDate.of(2015, 8, 19), new Detail("H-IIB", "HTV-5"),
            RocketStatus.STATUS_RETIRED, Optional.of(112.5), MissionStatus.SUCCESS);
    }

    @Test
    public void testMissionFromStringWillFullData() {
        String line =
            "1,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Thu Aug 06, 2020\",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,\"29.75 \",Success";

        Mission expected = new Mission("1", "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
            LocalDate.of(2020, 8, 6), new Detail("Long March 2D", "Gaofen-9 04 & Q-SAT"),
            RocketStatus.STATUS_ACTIVE, Optional.of(29.75), MissionStatus.SUCCESS);

        assertEquals(expected, Mission.from(line));
    }

    @Test
    public void testMissionFromStringWithoutCost() {
        String line =
            "413,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Thu Sep 01, 2016\",Falcon 9 Block 3 | AMOS-6,StatusRetired,,Prelaunch Failure";

        Mission expected = new Mission("413", "SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA",
            LocalDate.of(2016, 9, 1), new Detail("Falcon 9 Block 3", "AMOS-6"),
            RocketStatus.STATUS_RETIRED, Optional.empty(), MissionStatus.PRELAUNCH_FAILURE);

        assertEquals(expected, Mission.from(line));
    }

    @Test
    public void testGetCountry() {
        assertEquals("Japan", mission.getCountry());
    }

    @Test
    public void testIsBetweenWhenDateIsEqualToFrom() {
        assertTrue(mission.isBetween(LocalDate.of(2015, 8, 19), LocalDate.of(2015, 12, 12)));
    }

    @Test
    public void testIsBetweenWhenDateIsEqualToTo() {
        assertTrue(mission.isBetween(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 8, 19)));
    }

    @Test
    public void testIsBetween() {
        assertTrue(mission.isBetween(LocalDate.of(2015, 7, 19), LocalDate.of(2015, 9, 12)));
    }

    @Test
    public void testIsBetweenWhenNotInBetween() {
       assertFalse(mission.isBetween(LocalDate.of(2015, 9, 19), LocalDate.of(2015, 12, 12)));
    }
}
