package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.DrinkDeserializer;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.Requirement.requireNotNull;

public class TheCocktailDBClient implements TheCocktailDB {
    private static final String SEARCH_URI = "https://www.thecocktaildb.com/api/json/v1/1/search.php";
    private static final String FILTER_URI = "https://www.thecocktaildb.com/api/json/v1/1/filter.php";
    private static final String RANDOM_URI = "https://www.thecocktaildb.com/api/json/v1/1/random.php";
    private HttpClient httpClient;
    private Gson gson;

    private Logger logger;

    public TheCocktailDBClient(HttpClient httpClient, Logger logger) {
        this(httpClient,
            new GsonBuilder()
                .registerTypeAdapter(Drink.class, new DrinkDeserializer())
                .create(),
            logger);
    }

    public TheCocktailDBClient(HttpClient httpClient, Gson gson, Logger logger) {
        this.httpClient = httpClient;
        this.gson = gson;
        this.logger = logger;
    }

    @Override
    public Response searchDrinkByName(String name) {
        requireNotNull(name, "Name must not be null");

        return sendRequest(STR. "\{ SEARCH_URI }?s=\{ name }" );
    }

    @Override
    public Response searchDrinkByFirstLetter(char letter) {
        return sendRequest(STR. "\{ SEARCH_URI }?f=\{ letter }" );
    }

    @Override
    public Response searchIngredientByName(String name) {
        requireNotNull(name, "Name must not be null");

        return sendRequest(STR. "\{ SEARCH_URI }?i=\{ name }" );
    }

    @Override
    public Response searchRandomDrink() {
        return sendRequest(RANDOM_URI);
    }

    @Override
    public Response filterByIngredient(String ingredient) {
        requireNotNull(ingredient, "Ingredient must not be null");

        return sendRequest(STR. "\{ FILTER_URI }?i=\{ ingredient }" );
    }

    @Override
    public Response filterByAlcoholic(String type) {
        requireNotNull(type, "Type must not be null");

        return sendRequest(STR. "\{ FILTER_URI }?a=\{ type }" );
    }

    @Override
    public Response filterByCategory(String category) {
        requireNotNull(category, "Category must not be null");

        return sendRequest(STR. "\{ FILTER_URI }?c=\{ category }" );
    }

    @Override
    public Response filterByGlass(String glass) {
        requireNotNull(glass, "Glass must not be null");

        return sendRequest(STR. "\{ FILTER_URI }?g=\{ glass }" );
    }

    private Response sendRequest(String uri) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(uri.replace(" ", "%20"))).build();
            logger.log("Sending request to " + httpRequest.uri());

            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new UnsuccessfulRequestException(
                    "Unsuccessful request to TheCocktailDB with code: " + httpResponse.statusCode()
                );
            }
            return gson.fromJson(httpResponse.body(), Response.class);
        } catch (Exception e) {
            logger.log("Failed http request due to" + e.getMessage());
            throw new UnsuccessfulRequestException("An exception occurred while communicating with TheCocktailDB", e);
        }
    }
}
