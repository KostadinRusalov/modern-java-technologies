package bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetailTest {

    @Test
    public void testDetailFromString() {
        assertEquals(new Detail("Falcon 9 Block 5", "Starlink V1 L9 & BlackSky"),
            Detail.from("Falcon 9 Block 5 | Starlink V1 L9 & BlackSky"));
    }
}
