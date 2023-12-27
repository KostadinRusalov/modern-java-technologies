package bg.sofia.uni.fmi.mjt.space.mission;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetailTest {

    @Test
    public void testDetailFromString() {
        assertEquals(new Detail("Falcon 9 Block 5", "Starlink V1 L9 & BlackSky"),
            Detail.from("Falcon 9 Block 5 | Starlink V1 L9 & BlackSky"),
            "Rocket name and payload should be parsed correctly"
        );
    }

    @Test
    public void testDetailFromStringWithQuotes() {
        assertEquals(new Detail("Kuaizhou 1A", "Jilin-1 03, Caton-1 & Xingyun Shiyan 1"),
            Detail.from("Kuaizhou 1A | Jilin-1 03, Caton-1 & Xingyun Shiyan 1"),
            "Rocket name and payload should be parsed correctly when the data is surrounded by quotes"
        );
    }
}
