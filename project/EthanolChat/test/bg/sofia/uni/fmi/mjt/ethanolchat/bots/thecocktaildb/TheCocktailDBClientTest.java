package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Ingredient;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender.Common;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TheCocktailDBClientTest {
    @Mock
    HttpClient httpClient;

    @Mock
    Logger logger;

    @Mock
    Gson gson;

    @InjectMocks
    TheCocktailDBClient theCocktailDB;

    @Mock
    HttpResponse httpResponse;

    TheCocktailDB.Response response = new TheCocktailDB.Response(
        new Drink[] {Common.whiskey, Common.vodka},
        new Ingredient[] {new Ingredient("1", "ice", "cold water", "water", "0")}
    );

    String responseBody = "Body";

    void setUp() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(httpResponse.body()).thenReturn(responseBody);
        when(gson.fromJson(responseBody, TheCocktailDB.Response.class)).thenReturn(response);
    }

    @Test
    void testSearchDrinkByNameThrows() {
        assertThrows(UnsuccessfulRequestException.class, () -> theCocktailDB.searchDrinkByName("name"));
    }

    @Test
    void testSearchDrinkByNameUnsuccessfulRequest() throws Exception {
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        assertThrows(UnsuccessfulRequestException.class, () -> theCocktailDB.searchDrinkByName("name"));
    }

    @Test
    void testSearchDrinkByName() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.searchDrinkByName("whiskey"));
    }

    @Test
    void testSearchDrinkByFirstLetter() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.searchDrinkByFirstLetter('a'));
    }

    @Test
    void testSearchRandomDrink() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.searchRandomDrink());
    }

    @Test
    void testSearchIngredientByName() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.searchIngredientByName(""));
    }

    @Test
    void testFilterByIngredient() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.filterByIngredient(""));
    }

    @Test
    void testFilterByAlcoholic() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.filterByAlcoholic(""));
    }

    @Test
    void testFilterByCategory() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.filterByCategory(""));
    }

    @Test
    void testFilterByGlass() throws Exception {
        setUp();
        assertEquals(response, theCocktailDB.filterByGlass(""));
    }
}
