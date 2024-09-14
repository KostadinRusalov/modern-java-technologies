package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.DrinkDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DrinkDeserializerTest {

    @Test
    void testDeserialize() {
        String json = """
            {
                        "idDrink": "16108",
                        "strDrink": "9 1/2 Weeks",
                        "strCategory": "Cocktail",
                        "strAlcoholic": "Alcoholic",
                        "strGlass": "Cocktail glass",
                        "strInstructions": "Combine all ingredients in glass mixer. Chill and strain into Cocktail glass. Garnish with sliced strawberry.",
                        "strIngredient1": "Absolut Citron",
                        "strIngredient2": "Orange Curacao",
                        "strIngredient3": "Strawberry liqueur",
                        "strIngredient4": "Orange juice",
                        "strIngredient5": null,
                        "strIngredient6": null,
                        "strIngredient7": null,
                        "strIngredient8": null,
                        "strIngredient9": null,
                        "strIngredient10": null,
                        "strIngredient11": null,
                        "strIngredient12": null,
                        "strIngredient13": null,
                        "strIngredient14": null,
                        "strIngredient15": null,
                        "strMeasure1": "2 oz ",
                        "strMeasure2": "1/2 oz ",
                        "strMeasure3": "1 splash ",
                        "strMeasure4": "1 oz ",
                        "strMeasure5": null,
                        "strMeasure6": null,
                        "strMeasure7": null,
                        "strMeasure8": null,
                        "strMeasure9": null,
                        "strMeasure10": null,
                        "strMeasure11": null,
                        "strMeasure12": null,
                        "strMeasure13": null,
                        "strMeasure14": null,
                        "strMeasure15": null
                    }
            """;

        Drink expected = new Drink("16108", "9 1/2 Weeks", "Cocktail", "Alcoholic",
            "Cocktail glass",
            "Combine all ingredients in glass mixer. Chill and strain into Cocktail glass. Garnish with sliced strawberry.",
            new String[] {"Absolut Citron", "Orange Curacao", "Strawberry liqueur", "Orange juice"},
            new String[] {"2 oz", "1/2 oz", "1 splash", "1 oz"});

        Gson gson = new GsonBuilder().registerTypeAdapter(Drink.class, new DrinkDeserializer()).create();
        Drink actual = gson.fromJson(json, Drink.class);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.name(), actual.name());
        assertEquals(expected.category(), actual.category());
        assertEquals(expected.type(), actual.type());
        assertEquals(expected.glass(), actual.glass());
        assertEquals(expected.instructions(), actual.instructions());
        assertArrayEquals(expected.ingredients(), actual.ingredients());
        assertArrayEquals(expected.measurements(), actual.measurements());
    }
}
