package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.data.CSVReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.FAILURE;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.PARTIAL_FAILURE;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.PRELAUNCH_FAILURE;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.SUCCESS;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus.STATUS_ACTIVE;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus.STATUS_RETIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissionTest {

    private static Mission mission;

    @BeforeAll
    public static void setUp() {
        mission = new Mission("494", "MHI", "LA-Y2, Tanegashima Space Center, Japan",
            LocalDate.of(2015, 8, 19), new Detail("H-IIB", "HTV-5"),
            STATUS_RETIRED, Optional.of(112.5), SUCCESS);
    }

    @Test
    public void testMissionFromStringWillFullData() {
        String line =
            "1,CASC,\"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China\",\"Thu Aug 06, 2020\",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,\"29.75 \",Success";

        Mission expected = new Mission("1", "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
            LocalDate.of(2020, 8, 6), new Detail("Long March 2D", "Gaofen-9 04 & Q-SAT"),
            STATUS_ACTIVE, Optional.of(29.75), SUCCESS);

        assertEquals(expected, Mission.from(line), "Mission with full data should be parsed correctly");
    }

    @Test
    public void testMissionFromStringWithoutCost() {
        String line =
            "413,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Thu Sep 01, 2016\",Falcon 9 Block 3 | AMOS-6,StatusRetired,,Prelaunch Failure";

        Mission expected = new Mission("413", "SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA",
            LocalDate.of(2016, 9, 1), new Detail("Falcon 9 Block 3", "AMOS-6"),
            STATUS_RETIRED, Optional.empty(), PRELAUNCH_FAILURE);

        assertEquals(expected, Mission.from(line), "Mission without cost data should be parsed correctly");
    }

    @Test
    public void testGetCountry() {
        assertEquals("Japan", mission.getCountry(), "Country should be parsed correctly");
    }

    @Test
    public void testGetRocketStatus() {
        assertEquals("StatusRetired", mission.rocketStatus().toString(), "Rocket status should be parsed correctly");
    }

    @Test
    public void testGetMissionStatus() {
        assertEquals("Success", mission.missionStatus().toString(), "Mission status should be parsed correctly");
    }

    @Test
    public void testIsBetweenWhenDateIsEqualToFrom() {
        assertTrue(mission.isBetweenInclusive(LocalDate.of(2015, 8, 19), LocalDate.of(2015, 12, 12)),
            "Mission should be between two dates when the first date is the mission's date");
    }

    @Test
    public void testIsBetweenWhenDateIsEqualToTo() {
        assertTrue(mission.isBetweenInclusive(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 8, 19)),
            "Mission should be between two dates when the second date is the mission's date");
    }

    @Test
    public void testIsBetween() {
        assertTrue(mission.isBetweenInclusive(LocalDate.of(2015, 7, 19), LocalDate.of(2015, 9, 12)),
            "Mission should be between two dates when they are not inclusive");
    }

    @Test
    public void testIsBetweenWhenNotInBetween() {
        assertFalse(mission.isBetweenInclusive(LocalDate.of(2015, 9, 19), LocalDate.of(2015, 12, 12)),
            "Mission should not be between two dates");
    }

    @Test
    public void testReadCSV() {
        String csv = """
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            105,VKS RF,"Site 133/3, Plesetsk Cosmodrome, Russia","Fri Aug 30, 2019",Rokot/Briz KM | Cosmos 2540,StatusRetired,"41.8 ",Success
            106,ISA,"Imam Khomeini Spaceport, Semnan Space Center, Iran","Thu Aug 29, 2019",Safir-1B+ | Nahid-1,StatusActive,,Prelaunch Failure
            4298,RVSN USSR,"Site 1/5, Baikonur Cosmodrome, Kazakhstan","Sat Oct 11, 1958",Vostok | E-1 n†­2 (Luna-1),StatusRetired,,Failure
            4299,NASA,"SLC-17A, Cape Canaveral AFS, Florida, USA","Sat Oct 11, 1958",Thor-DM 18 Able I | Pioneer 1,StatusRetired,,Partial Failure
            """;

        List<Mission> expected = List.of(
            new Mission("105", "VKS RF", "Site 133/3, Plesetsk Cosmodrome, Russia", LocalDate.of(2019, 8, 30),
                new Detail("Rokot/Briz KM", "Cosmos 2540"), STATUS_RETIRED, Optional.of(41.8), SUCCESS),
            new Mission("106", "ISA", "Imam Khomeini Spaceport, Semnan Space Center, Iran", LocalDate.of(2019, 8, 29),
                new Detail("Safir-1B+", "Nahid-1"), STATUS_ACTIVE, Optional.empty(), PRELAUNCH_FAILURE),
            new Mission("4298", "RVSN USSR", "Site 1/5, Baikonur Cosmodrome, Kazakhstan", LocalDate.of(1958, 10, 11),
                new Detail("Vostok", "E-1 n†­2 (Luna-1)"), STATUS_RETIRED, Optional.empty(), FAILURE),
            new Mission("4299", "NASA", "SLC-17A, Cape Canaveral AFS, Florida, USA", LocalDate.of(1958, 10, 11),
                new Detail("Thor-DM 18 Able I", "Pioneer 1"), STATUS_RETIRED, Optional.empty(), PARTIAL_FAILURE)
        );

        assertEquals(expected, CSVReader.read(new StringReader(csv), Mission::from),
            "Missions should be read correctly when reading from a csv reader");
    }
}
