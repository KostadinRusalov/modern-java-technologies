package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static bg.sofia.uni.fmi.mjt.football.Position.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FootballPlayerAnalyzerTest {
    private static FootballPlayerAnalyzer analyzer;
    private static Player player;

    @BeforeAll
    public static void setUp() {
        String file = """
            name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left
            C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right
            P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right
            K. Koulibaly;Kalidou Koulibaly;6/20/1991;27;187.96;88.9;CB;Senegal;88;91;60000000;135000;Right
            V. van Dijk;Virgil van Dijk;7/8/1991;27;193.04;92.1;CB;Netherlands;88;90;59500000;215000;Right
            K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right
            S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right
            M. Neuer;Manuel Neuer;3/27/1986;32;193.04;92.1;GK;Germany;89;89;38000000;130000;Right
            E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right
            Sergio Busquets;Sergio Busquets i Burgos;7/16/1988;30;187.96;76.2;CDM,CM;Spain;89;89;51500000;315000;Right
            T. Courtois;Thibaut Courtois;5/11/1992;26;198.12;96.2;GK;Belgium;89;90;53500000;240000;Left
            M. ter Stegen;Marc-André ter Stegen;4/30/1992;26;187.96;84.8;GK;Germany;89;92;58000000;240000;Right
            A. Griezmann;Antoine Griezmann;3/21/1991;27;175.26;73;CF,ST;France;89;90;78000000;145000;Left
            J. Rodríguez;James David Rodríguez Rubio;7/12/1991;27;154.94;74.8;CAM,CM,RM;Colombia;87;88;59000000;260000;Left
            """;
        analyzer = new FootballPlayerAnalyzer(new StringReader(file));
        player =
            Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right");
    }

    @Test
    public void testGetAllPlayers() {
        assertEquals(15, analyzer.getAllPlayers().size());
    }

    @Test
    public void testGetAllNationalities() {
        assertEquals(Set.of(
                "Italy", "Argentina", "Spain", "Senegal", "Netherlands",
                "Germany", "Denmark", "Colombia", "France", "Uruguay", "Belgium"
            ),
            analyzer.getAllNationalities()
        );
    }

    @Test
    public void testHighestPaidNullNationality() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getHighestPaidPlayerByNationality(null));
    }

    @Test
    public void testHighestPaidInvalidNationality() {
        assertThrows(NoSuchElementException.class, () -> analyzer.getHighestPaidPlayerByNationality("Bulgaria"));
    }

    @Test
    public void testGroupByPosition() {
        assertEquals(Set.of(ST, CF, GK, RW, CM, LW, CDM, CAM, RM, CB), analyzer.groupByPosition().keySet());
    }

    @Test
    public void testGetTopPlayerNullPosition() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getTopProspectPlayerForPositionInBudget(null, 0));
    }

    @Test
    public void testGetTopPlayerNegativeBudget() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getTopProspectPlayerForPositionInBudget(CB, -10L));
    }

    @Test
    public void testGetTopPlayerWithLowBudget() {
        assertEquals(Optional.empty(), analyzer.getTopProspectPlayerForPositionInBudget(ST, 0L));
    }

    @Test
    public void testGetTopPlayer() {
        assertEquals(Optional.of(player), analyzer.getTopProspectPlayerForPositionInBudget(ST, (long) 1e8));
    }

    @Test
    public void testGetSimilarPlayersToNull() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getSimilarPlayers(null));

    }
    @Test
    public void testGetSimilarPlayers() {
        Player player1 = Player.of(
            "S. Agüero;Sergio Leonel Agüero del Castillo;6/2/1988;30;172.72;69.9;ST;Argentina;89;89;64500000;300000;Right"
        );
        Player player2 = Player.of(
            "L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;Right"
        );
        Player player3 = Player.of(
            "E. Cavani;Edinson Roberto Cavani Gómez;2/14/1987;32;185.42;77.1;ST;Uruguay;89;89;60000000;200000;Right"
        );
        Player player4 = Player.of(
            "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right"
        );

        assertEquals(Set.of(player, player1, player2, player3, player4),
            analyzer.getSimilarPlayers(player));
    }

    @Test
    public void testGetPlayersByNullKeyword() {
        assertThrows(IllegalArgumentException.class, () -> analyzer.getPlayersByFullNameKeyword(null));
    }

    @Test
    public void testGetPlayersByKeyword() {
        assertEquals(Set.of(player), analyzer.getPlayersByFullNameKeyword("Kylian"));
    }
}
