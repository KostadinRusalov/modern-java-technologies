package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.Hit;
import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.Link;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collection;

public record EdamamResponse(@SerializedName("_links") Link link, Hit[] hits) {
    private static final Gson GSON = new Gson();

    public static EdamamResponse from(String json) {
        return GSON.fromJson(json, EdamamResponse.class);
    }

    public Collection<Recipe> getRecipes() {
        return Arrays.stream(hits).map(Hit::recipe).toList();
    }
}
