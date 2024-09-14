package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Ingredient;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.UnsuccessfulRequestException;
import com.google.gson.annotations.SerializedName;

public interface TheCocktailDB {
    /**
     * Searches for all drinks with that name.
     *
     * @param name part of the name of the drink
     * @return response with an array of drinks or with drinks == null
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response searchDrinkByName(String name);

    /**
     * Searches for all drinks which name starts with the letter.
     *
     * @param letter first letter of the name of the drink
     * @return response with an array of drinks or with drinks == null, or null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response searchDrinkByFirstLetter(char letter);

    /**
     * Searches for an ingredient with given name.
     *
     * @param name name of the ingredient
     * @return response with an array of one ingredient or with ingredients == null
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response searchIngredientByName(String name);

    /**
     * Searches for a random drink.
     *
     * @return response with an array of one drink
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response searchRandomDrink();

    /**
     * Filters drinks by ingredient.
     *
     * @param ingredient the filter
     * @return response with an array of drinks or null if there's no such ingredient
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response filterByIngredient(String ingredient);

    /**
     * Filters drinks by alcoholic type.
     *
     * @param type can be Alcoholic, Non Alcoholic or Optional Alcoholic
     * @return response with an array of drinks or null if the type is incorrect
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response filterByAlcoholic(String type);

    /**
     * Filters drinks by category.
     *
     * @param category the filter
     * @return response with an array of drinks or null if the category is incorrect
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response filterByCategory(String category);

    /**
     * Filters drinks by glass.
     *
     * @param glass the filter
     * @return response with an array of drinks or null if the glass is incorrect
     * @throws IllegalArgumentException     if name is null
     * @throws UnsuccessfulRequestException if the request cannot be made
     */
    Response filterByGlass(String glass);

    record Response(@SerializedName("drinks") Drink[] drinks,
                    @SerializedName("ingredients") Ingredient[] ingredients) {
    }
}
