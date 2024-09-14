package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models;

import com.google.gson.annotations.SerializedName;

public record Ingredient(@SerializedName("idIngredient") String id,
                         @SerializedName("strIngredient") String name,
                         @SerializedName("strDescription") String description,
                         @SerializedName("strType") String type,
                         @SerializedName("strABV") String abv) {
}