package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.cache;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.TheCocktailDB;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;

import static bg.sofia.uni.fmi.mjt.ethanolchat.server.exception.Requirement.requireNotNull;

public class TheCocktailDBCacheProxy implements TheCocktailDB {
    private TheCocktailDB theCocktailDB;
    private Cache<String, Response> drinksCache;
    private Cache<String, Response> ingredientsCache;
    private Cache<Character, Response> drinksByLetterCache;
    private Cache<String, Response> ingredientFilterCache;
    private Cache<String, Response> alcoholicFilterCache;
    private Cache<String, Response> categoryFilterCache;
    private Cache<String, Response> glassFilterCache;

    public TheCocktailDBCacheProxy(TheCocktailDB theCocktailDB,
                                   Cache<String, Response> drinksCache,
                                   Cache<String, Response> ingredientsCache,
                                   Cache<Character, Response> drinksByLetterCache,
                                   Cache<String, Response> ingredientFilterCache,
                                   Cache<String, Response> alcoholicFilterCache,
                                   Cache<String, Response> categoryFilterCache,
                                   Cache<String, Response> glassFilterCache) {
        this.theCocktailDB = theCocktailDB;
        this.drinksCache = drinksCache;
        this.ingredientsCache = ingredientsCache;
        this.drinksByLetterCache = drinksByLetterCache;
        this.ingredientFilterCache = ingredientFilterCache;
        this.alcoholicFilterCache = alcoholicFilterCache;
        this.categoryFilterCache = categoryFilterCache;
        this.glassFilterCache = glassFilterCache;
    }

    @Override
    public Response searchDrinkByName(String name) {
        requireNotNull(name, "Name must not be null");

        Response response = drinksCache.cache(name, theCocktailDB::searchDrinkByName);
        cacheAllDrinks(response);
        return response;
    }

    @Override
    public Response searchDrinkByFirstLetter(char letter) {
        Response response = drinksByLetterCache.cache(letter, theCocktailDB::searchDrinkByFirstLetter);
        cacheAllDrinks(response);
        return response;
    }

    @Override
    public Response searchIngredientByName(String name) {
        requireNotNull(name, "Name must not be null");

        return ingredientsCache.cache(name, theCocktailDB::searchIngredientByName);
    }

    @Override
    public Response searchRandomDrink() {
        return theCocktailDB.searchRandomDrink();
    }

    @Override
    public Response filterByIngredient(String ingredient) {
        requireNotNull(ingredient, "Ingredient must not be null");

        return ingredientFilterCache.cache(ingredient, theCocktailDB::filterByIngredient);
    }

    @Override
    public Response filterByAlcoholic(String type) {
        requireNotNull(type, "Type must not be null");

        return alcoholicFilterCache.cache(type, theCocktailDB::filterByAlcoholic);
    }

    @Override
    public Response filterByCategory(String category) {
        requireNotNull(category, "Category must not be null");

        return categoryFilterCache.cache(category, theCocktailDB::filterByCategory);
    }

    @Override
    public Response filterByGlass(String glass) {
        requireNotNull(glass, "Glass must not be null");

        return glassFilterCache.cache(glass, theCocktailDB::filterByGlass);
    }

    private void cacheAllDrinks(Response response) {
        if (response.drinks() == null) {
            return;
        }

        for (Drink drink : response.drinks()) {
            String name = drink.name().toLowerCase();
            if (!drinksCache.has(name)) {
                drinksCache.cache(name, new Response(new Drink[] {drink}, null));
            }
        }
    }
}
