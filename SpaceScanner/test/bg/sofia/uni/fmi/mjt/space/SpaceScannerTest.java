package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.PRELAUNCH_FAILURE;
import static bg.sofia.uni.fmi.mjt.space.mission.MissionStatus.SUCCESS;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus.STATUS_ACTIVE;
import static bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus.STATUS_RETIRED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SpaceScannerTest {
    private static SpaceScannerAPI spaceScanner;
    private static SecretKey secretKey;

    @BeforeAll
    public static void setUp() throws NoSuchAlgorithmException {
        secretKey = KeyGenerator.getInstance(Rijndael.ALGORITHM).generateKey();

        String missions = """
            header
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
            12,CASC,"LC-3, Xichang Satellite Launch Center, China","Thu Jul 09, 2020",Long March 3B/E | Apstar-6D,StatusActive,"29.15 ",Success
            36,CASC,"LC-2, Xichang Satellite Launch Center, China","Thu Apr 09, 2020",Long March 3B/E | Nusantara Dua,StatusActive,"29.15 ",Failure
            233,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Fri Jun 29, 2018",Falcon 9 Block 4 | CRS-15,StatusRetired,"62.0 ",Success
            413,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Thu Sep 01, 2016",Falcon 9 Block 3 | AMOS-6,StatusRetired,"62.0 ",Prelaunch Failure
            499,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Sun Jun 28, 2015",Falcon 9 v1.1 | CRS-7,StatusRetired,"56.5 ",Failure
            587,CASIC,"Jiuquan Satellite Launch Center, China","Wed Sep 25, 2013",Kuaizhou 1 | Kuaizhou 1,StatusRetired,,Success
            629,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Oct 08, 2012",Falcon 9 v1.0 | CRS-1,StatusRetired,"59.5 ",Partial Failure
            682,NASA,"LC-39A, Kennedy Space Center, Florida, USA","Fri Jul 08, 2011",Space Shuttle Atlantis | STS-135,StatusRetired,"450.0 ",Success
            974,Boeing,"SLC-37B, Cape Canaveral AFS, Florida, USA","Tue Dec 21, 2004",Delta IV Heavy | DemoSat and 3CS-1 & 2,StatusActive,"350.0 ",Partial Failure
            1299,RVSN USSR,"Site 41/1, Plesetsk Cosmodrome, Russia","Wed Aug 12, 1998",Molniya-M /Block ML | Molniya-1 n†­133,StatusRetired,,Success
            1429,CASC,"LC-3, Xichang Satellite Launch Center, China","Sun Aug 18, 1996",Long March 3 | Chinasat-7,StatusRetired,,Partial Failure
            1923,RVSN USSR,"Site 41/1, Plesetsk Cosmodrome, Russia","Thu Sep 29, 1988",Molniya-M /Block ML | Molniya-3 n†­134,StatusRetired,,Success
            2000,RVSN USSR,"Site 250, Baikonur Cosmodrome, Kazakhstan","Fri May 15, 1987",Energiya/Polyus | Polyus Space Station,StatusRetired,"5,000.0 ",Success
            3545,NASA,"LC-39A, Kennedy Space Center, Florida, USA","Wed Jul 16, 1969",Saturn V | Apollo 11,StatusRetired,"1,160.0 ",Success
            """;

        String rockets = """
            header
            62,Atlas-E/F Burner,,
            109,Ceres-1,,19.0 m
            150,"Delta IV Medium+ (5,4)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
            159,Energiya/Polyus,https://en.wikipedia.org/wiki/Energia#Development,59.0 m
            168,Falcon 9 Block 4,https://en.wikipedia.org/wiki/Falcon_9,70.0 m
            315,Saturn V,https://en.wikipedia.org/wiki/Saturn_V,110.6 m
            362,Space Shuttle Atlantis,https://en.wikipedia.org/wiki/Space_Shuttle_Atlantis,56.1 m
            """;

        spaceScanner = new MJTSpaceScanner(new StringReader(missions), new StringReader(rockets), secretKey);
    }

    @Test
    public void testGetAllMissions() {
        assertEquals(18, spaceScanner.getAllMissions().size());
    }

    @Test
    public void testGetAllMissionsWithNullStatusThrows() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getAllMissions(null));
    }

    @Test
    public void testGetAllMissionsWithPreLaunchFailure() {
        assertEquals(List.of(
            Mission.from(
                "413,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Thu Sep 01, 2016\",Falcon 9 Block 3 | AMOS-6,StatusRetired,\"62.0 \",Prelaunch Failure")
        ), spaceScanner.getAllMissions(PRELAUNCH_FAILURE));
    }

    @Test
    public void testGetCompanyWithMostSuccessfulMissionsNullFromDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, LocalDate.of(2022, 2, 1)));
    }

    @Test
    public void testGetCompanyWithMostSuccessfulMissionsNullToDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.of(2022, 2, 22), null));
    }

    @Test
    public void testGetCompanyWithMostSuccessfulMissionsThrowsTimeFrameMismatchException() {
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.of(2022, 2, 22),
                LocalDate.of(2021, 1, 2)));
    }

    @Test
    public void testGetCompanyWithMostSuccessfulMissions() {
        assertEquals("SpaceX", spaceScanner.getCompanyWithMostSuccessfulMissions(
            LocalDate.of(2020, 1, 1), LocalDate.of(2020, 12, 30))
        );
    }

    @Test
    public void testGetMissionsPerCountry() {
        Map<String, Collection<Mission>> expected = Map.ofEntries(
            Map.entry("USA", Mission.readCSV(new StringReader("""
                header
                0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
                8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
                413,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Thu Sep 01, 2016",Falcon 9 Block 3 | AMOS-6,StatusRetired,"62.0 ",Prelaunch Failure
                499,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Sun Jun 28, 2015",Falcon 9 v1.1 | CRS-7,StatusRetired,"56.5 ",Failure
                682,NASA,"LC-39A, Kennedy Space Center, Florida, USA","Fri Jul 08, 2011",Space Shuttle Atlantis | STS-135,StatusRetired,"450.0 ",Success
                3545,NASA,"LC-39A, Kennedy Space Center, Florida, USA","Wed Jul 16, 1969",Saturn V | Apollo 11,StatusRetired,"1,160.0 ",Success
                2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
                629,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Oct 08, 2012",Falcon 9 v1.0 | CRS-1,StatusRetired,"59.5 ",Partial Failure
                233,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Fri Jun 29, 2018",Falcon 9 Block 4 | CRS-15,StatusRetired,"62.0 ",Success
                974,Boeing,"SLC-37B, Cape Canaveral AFS, Florida, USA","Tue Dec 21, 2004",Delta IV Heavy | DemoSat and 3CS-1 & 2,StatusActive,"350.0 ",Partial Failure
                """
            ))),
            Map.entry("China", Mission.readCSV(new StringReader("""
                header
                12,CASC,"LC-3, Xichang Satellite Launch Center, China","Thu Jul 09, 2020",Long March 3B/E | Apstar-6D,StatusActive,"29.15 ",Success
                587,CASIC,"Jiuquan Satellite Launch Center, China","Wed Sep 25, 2013",Kuaizhou 1 | Kuaizhou 1,StatusRetired,,Success
                36,CASC,"LC-2, Xichang Satellite Launch Center, China","Thu Apr 09, 2020",Long March 3B/E | Nusantara Dua,StatusActive,"29.15 ",Failure
                1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
                1429,CASC,"LC-3, Xichang Satellite Launch Center, China","Sun Aug 18, 1996",Long March 3 | Chinasat-7,StatusRetired,,Partial Failure
                """
            ))),
            Map.entry("Russia", Mission.readCSV(new StringReader("""
                header
                1923,RVSN USSR,"Site 41/1, Plesetsk Cosmodrome, Russia","Thu Sep 29, 1988",Molniya-M /Block ML | Molniya-3 n†\u00AD134,StatusRetired,,Success
                1299,RVSN USSR,"Site 41/1, Plesetsk Cosmodrome, Russia","Wed Aug 12, 1998",Molniya-M /Block ML | Molniya-1 n†\u00AD133,StatusRetired,,Success
                """
            ))),
            Map.entry("Kazakhstan", List.of(Mission.from(
                "2000,RVSN USSR,\"Site 250, Baikonur Cosmodrome, Kazakhstan\",\"Fri May 15, 1987\",Energiya/Polyus | Polyus Space Station,StatusRetired,\"5,000.0 \",Success")
            ))
        );

        Map<String, Collection<Mission>> actual = spaceScanner.getMissionsPerCountry();

        assertTrue(
            expected.get("USA").containsAll(actual.get("USA")) && actual.get("USA").containsAll(expected.get("USA")));
        assertTrue(expected.get("China").containsAll(actual.get("China")) &&
            actual.get("China").containsAll(expected.get("China")));
        assertTrue(expected.get("Russia").containsAll(actual.get("Russia")) &&
            actual.get("Russia").containsAll(expected.get("Russia")));
        assertTrue(expected.get("Kazakhstan").containsAll(actual.get("Kazakhstan")) &&
            actual.get("Kazakhstan").containsAll(expected.get("Kazakhstan")));
    }

    @Test
    public void testGetTopNLeastExpensiveMissionsNegativeN() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getTopNLeastExpensiveMissions(-1, SUCCESS, STATUS_ACTIVE));
    }

    @Test
    public void testGetTopNLeastExpensiveMissionsNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getTopNLeastExpensiveMissions(3, null, STATUS_ACTIVE));
    }

    @Test
    public void testGetTopNLeastExpensiveMissionsNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getTopNLeastExpensiveMissions(3, SUCCESS, null));
    }

    @Test
    public void testGetTopNLeastExpensiveMissions() {
        assertEquals(Mission.readCSV(new StringReader("""
            header
            12,CASC,"LC-3, Xichang Satellite Launch Center, China","Thu Jul 09, 2020",Long March 3B/E | Apstar-6D,StatusActive,"29.15 ",Success
            1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Long March 2D | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            """
        )), spaceScanner.getTopNLeastExpensiveMissions(3, SUCCESS, STATUS_ACTIVE));
    }

    @Test
    public void testGetMostDesiredLocationForMissionsPerCompany() {
        Map<String, String> expected = Map.ofEntries(
            Map.entry("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA"),
            Map.entry("CASIC", "Jiuquan Satellite Launch Center, China"),
            Map.entry("CASC", "LC-3, Xichang Satellite Launch Center, China"),
            Map.entry("RVSN USSR", "Site 41/1, Plesetsk Cosmodrome, Russia"),
            Map.entry("Boeing", "SLC-37B, Cape Canaveral AFS, Florida, USA"),
            Map.entry("NASA", "LC-39A, Kennedy Space Center, Florida, USA")
        );

        assertEquals(expected, spaceScanner.getMostDesiredLocationForMissionsPerCompany());
    }

    @Test
    public void testGetLocationWithMostSuccessfulMissionNullFromDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.of(2020, 2, 2)));
    }

    @Test
    public void testGetLocationWithMostSuccessfulMissionNullToDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.of(2020, 2, 2), null));
    }
    @Test
    public void testGetLocationWithMostSuccessfulMissionTimeFrameMismatch() {
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.of(2030, 2, 2), LocalDate.of(2020, 2, 2)));
    }

    @Test
    public void testGetLocationWithMostSuccessfulMissionsPerCompany() {
        Map<String, String> expected = Map.ofEntries(
            Map.entry("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA"),
            Map.entry("CASIC", "Jiuquan Satellite Launch Center, China"),
            Map.entry("CASC", "LC-3, Xichang Satellite Launch Center, China"),
            Map.entry("RVSN USSR", "Site 41/1, Plesetsk Cosmodrome, Russia"),
            Map.entry("NASA", "LC-39A, Kennedy Space Center, Florida, USA")
        );

        assertEquals(expected,
            spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(
                LocalDate.of(1987, 4, 22),
                LocalDate.of(2020, 7, 20))
        );
    }

    @Test
    public void testGetAllRockets() {
        assertEquals(7, spaceScanner.getAllRockets().size());
    }

    @Test
    public void testGetTopNTallestRocketsNegativeN() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getTopNTallestRockets(-5));
    }

    @Test
    public void testGetTopNTallestRockets() {
        List<Rocket> expected = Rocket.readCSV(new StringReader("""
            header
            315,Saturn V,https://en.wikipedia.org/wiki/Saturn_V,110.6 m
            168,Falcon 9 Block 4,https://en.wikipedia.org/wiki/Falcon_9,70.0 m
            150,"Delta IV Medium+ (5,4)",https://en.wikipedia.org/wiki/Delta_IV,66.4 m
            159,Energiya/Polyus,https://en.wikipedia.org/wiki/Energia#Development,59.0 m
            362,Space Shuttle Atlantis,https://en.wikipedia.org/wiki/Space_Shuttle_Atlantis,56.1 m
            """)
        );

        assertEquals(expected, spaceScanner.getTopNTallestRockets(5));
    }

    @Test
    public void testGetWikiPageForRocket() {
        var expected = Map.ofEntries(
            Map.entry("Atlas-E/F Burner", Optional.empty()),
            Map.entry("Ceres-1", Optional.empty()),
            Map.entry("Delta IV Medium+ (5,4)", Optional.of("https://en.wikipedia.org/wiki/Delta_IV")),
            Map.entry("Energiya/Polyus", Optional.of("https://en.wikipedia.org/wiki/Energia#Development")),
            Map.entry("Falcon 9 Block 4", Optional.of("https://en.wikipedia.org/wiki/Falcon_9")),
            Map.entry("Saturn V", Optional.of("https://en.wikipedia.org/wiki/Saturn_V")),
            Map.entry("Space Shuttle Atlantis",
                Optional.of("https://en.wikipedia.org/wiki/Space_Shuttle_Atlantis"))
        );

        assertEquals(expected, spaceScanner.getWikiPageForRocket());
    }

    @Test
    public void testGetWikiPagesForRocketsInMostExpensiveMissionsNegativeN() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-1, SUCCESS, STATUS_ACTIVE));
    }

    @Test
    public void testGetWikiPagesForRocketsInMostExpensiveMissionsNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, null, STATUS_ACTIVE));
    }

    @Test
    public void testGetWikiPagesForRocketsInMostExpensiveMissionsNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, SUCCESS, null));
    }

    @Test
    public void testGetWikiPagesForRocketsUsedInMostExpensiveMissions() {
        List<String> expected = List.of(
            "https://en.wikipedia.org/wiki/Energia#Development",
            "https://en.wikipedia.org/wiki/Saturn_V",
            "https://en.wikipedia.org/wiki/Space_Shuttle_Atlantis",
            "https://en.wikipedia.org/wiki/Falcon_9"
        );

        List<String> actual = spaceScanner
            .getWikiPagesForRocketsUsedInMostExpensiveMissions(4, SUCCESS, STATUS_RETIRED);

        assertTrue(expected.containsAll(actual) && actual.containsAll(expected));
    }

    @Test
    public void testSaveMostReliableRocketWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.saveMostReliableRocket(null, LocalDate.of(2020, 2, 2), LocalDate.of(2021, 1, 1)));
    }

    @Test
    public void testSaveMostReliableRocketWithNullFromDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), null, LocalDate.of(2021, 1, 1)));
    }

    @Test
    public void testSaveMostReliableRocketWithNullToDate() {
        assertThrows(IllegalArgumentException.class,
            () -> spaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), LocalDate.of(2020, 2, 2), null));
    }


    @Test
    public void testSaveMostReliableRocketWithTimeFrameMismatch() {
        assertThrows(TimeFrameMismatchException.class,
            () -> spaceScanner.saveMostReliableRocket(new ByteArrayOutputStream(), LocalDate.of(2021, 2, 2), LocalDate.of(2020, 1, 1)));
    }
    @Test
    public void testSaveMostReliableRocketWhenThereIsnTOne() throws CipherException {
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();
        spaceScanner.saveMostReliableRocket(encryptedOutput, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 27));

        ByteArrayInputStream encryptedInput = new ByteArrayInputStream(encryptedOutput.toByteArray());
        ByteArrayOutputStream decryptedOutput = new ByteArrayOutputStream();
        new Rijndael(secretKey).decrypt(encryptedInput, decryptedOutput);

        assertEquals("", decryptedOutput.toString());
    }

    @Test
    public void testSaveMostReliableRocket() throws CipherException {
        ByteArrayOutputStream encryptedOutput = new ByteArrayOutputStream();
        spaceScanner.saveMostReliableRocket(encryptedOutput, LocalDate.of(2016, 8, 27), LocalDate.of(2019, 5, 13));

        ByteArrayInputStream encryptedInput = new ByteArrayInputStream(encryptedOutput.toByteArray());
        ByteArrayOutputStream decryptedOutput = new ByteArrayOutputStream();
        new Rijndael(secretKey).decrypt(encryptedInput, decryptedOutput);

        assertEquals("Falcon 9 Block 4", decryptedOutput.toString());
    }
}