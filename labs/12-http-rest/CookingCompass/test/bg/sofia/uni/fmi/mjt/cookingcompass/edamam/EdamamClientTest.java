package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.EdamamRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.http.HttpClient;

public class EdamamClientTest {

    @Mock
    private HttpClient client;

    @InjectMocks
    private EdamamClient edamamClient;

    @Test
    public void testFindRequest() {
        EdamamRequest request = EdamamRequest.builder()
            .withQuery("chicken")
            .build();

        // DISCLAIMER: due to poor time management there was no time to write tests for this class :(
    }
}
