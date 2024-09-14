package bg.sofia.uni.fmi.mjt.ethanolchat.server.commands.bartender;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Ingredient;
import bg.sofia.uni.fmi.mjt.ethanolchat.server.models.WishList;

import java.util.Arrays;

public class BartenderCommandUtils {
    public static final String UNSUCCESSFUL_REQUEST_EXCEPTION = "Sorry, the request was unsuccessful.";
    public static final String UNSUCCESSFUL_REQUEST = "Sorry, no items match your request.";
    public static final String INVALID_FILTER = "Oops, incorrect filter!";
    public static final String CANNOT_ADD_TO_WISHLIST = "Sorry, there is no drink to add.";
    public static final String CANNOT_REMOVE_FROM_WISHLIST = "Sorry, cannot remove the drink.";
    public static final String ADDED_TO_WISHLIST = "Hey, the drink is added to the wishlist.";
    public static final String ALREADY_IN_WISHLIST = "Hey, the drinks is already added to the wishlist.";
    public static final String NO_WISHLIST = "Sorry, there is no wishlist.";
    public static final String REMOVED = "Hey, the drink was just removed from the wishlist.";
    public static final String NO_DRINK = "Hey, there was no such drink in the wishlist.";
    public static final String CANNOT_SHARE_WISHLIST = "Sorry, there's a problem with sharing the wishlist.";
    private static final String INFO = "Hey, here's the info about ";

    public static String formatDrinks(Drink[] drinks) {
        if (drinks == null) {
            return UNSUCCESSFUL_REQUEST;
        }
        if (drinks.length == 1) {
            return STR. "Hey, there's 1 drink that matches your request: \{ drinks[0].name() }." ;
        }
        return STR. "Hey, there are \{ drinks.length } that match your request: \{
            String.join(", ", Arrays.stream(drinks).map(Drink::name).toList())
            }." ;
    }

    public static String formatIngredientsInfo(Ingredient[] ingredients) {
        if (ingredients == null) {
            return UNSUCCESSFUL_REQUEST;
        }

        Ingredient ingredient = ingredients[0];
        StringBuilder response = new StringBuilder(INFO).append(ingredient.name());
        if (ingredient.description() != null) {
            response.append(System.lineSeparator()).append("description: ").append(ingredient.description());
        }

        if (ingredient.type() != null) {
            response.append(System.lineSeparator()).append("type: ").append(ingredient.type());
        }

        if (ingredient.abv() != null) {
            response.append(System.lineSeparator()).append("ABV: ").append(ingredient.abv());
        }

        return response.toString();
    }

    public static String formatWishListDrinks(WishList wishList) {
        if (wishList == null || wishList.getDrinks().isEmpty()) {
            return NO_WISHLIST;
        }
        return STR. "Drinks in wishlist: \{ String.join(", ",
            wishList.getDrinks().stream().map(Drink::name).toList()) }" ;
    }

    public static String formatDrinksInfo(Drink[] drinks) {
        if (drinks == null) {
            return UNSUCCESSFUL_REQUEST;
        }
        return formatDrink(drinks[0]);
    }

    public static String formatUploadedFile(String downloadPage) {
        return STR. "Here's the wishlist: \{ downloadPage }" ;
    }

    private static String formatDrink(Drink drink) {
        StringBuilder response = new StringBuilder(INFO).append(drink.name());
        if (drink.category() != null) {
            response.append(System.lineSeparator()).append("category: ").append(drink.category());
        }
        if (drink.type() != null) {
            response.append(System.lineSeparator()).append("type: ").append(drink.type());
        }
        if (drink.glass() != null) {
            response.append(System.lineSeparator()).append("served in: ").append(drink.glass());
        }
        response.append(System.lineSeparator()).append("ingredients:");
        for (int i = 0; i < drink.ingredients().length; i++) {
            response.append(System.lineSeparator()).append("- ");
            if (drink.measurements()[i] != null) {
                response.append(drink.measurements()[i]).append(" ");
            }
            response.append(drink.ingredients()[i]);
        }
        if (drink.instructions() != null) {
            response.append(System.lineSeparator()).append("how to make: ").append(drink.instructions());
        }
        return response.toString();
    }
}
