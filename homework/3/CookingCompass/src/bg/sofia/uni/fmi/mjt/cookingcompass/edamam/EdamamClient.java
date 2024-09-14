package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.EdamamRequest;
import bg.sofia.uni.fmi.mjt.cookingcompass.exception.EdamamClientRequestException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers;

public class EdamamClient {
    private static EdamamClient instance = new EdamamClient();
    private HttpClient client;

    public EdamamClient() {
        this.client = HttpClient.newHttpClient();
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public EdamamRecipeBook find(EdamamRequest recipeRequest) {
        var edamamResponse = send(recipeRequest.getHttpRequest());
        return new EdamamRecipeBook(edamamResponse.getRecipes(), edamamResponse.link());
    }

    public static EdamamClient getInstance() {
        return instance;
    }

    public EdamamResponse send(String request) {
        return send(HttpRequest.newBuilder(URI.create(request)).build());
    }

    public EdamamResponse send(HttpRequest httpRequest) {
        try {
            HttpResponse<String> response = client.send(httpRequest, BodyHandlers.ofString());
            if (response.statusCode() != HttpURLConnection.HTTP_OK) {
                throw new EdamamClientRequestException(
                    "Request failed with status %d and errors %s".formatted(response.statusCode(), response.body())
                );
            }

            return EdamamResponse.from(response.body());
        } catch (IOException | InterruptedException e) {
            throw new EdamamClientRequestException("Request failed", e);
        }
    }
}
