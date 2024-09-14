package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;

public class DrinkDeserializer implements JsonDeserializer<Drink> {
    private static final String ID = "idDrink";
    private static final String NAME = "strDrink";
    private static final String CATEGORY = "strCategory";
    private static final String TYPE = "strAlcoholic";
    private static final String GLASS = "strGlass";
    private static final String INSTRUCTIONS = "strInstructions";
    private static final String INGREDIENT = "strIngredient";
    private static final String MEASURE = "strMeasure";
    public static final int MAX_INGREDIENTS = 15;

    @Override
    public Drink deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
        throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        JsonElement id = object.get(ID);
        JsonElement name = object.get(NAME);

        if (id.isJsonNull() || name.isJsonNull()) {
            return null;
        }

        String[] ingredients = new String[MAX_INGREDIENTS];
        String[] measurements = new String[MAX_INGREDIENTS];
        int i = 1;
        for (; i <= MAX_INGREDIENTS; i++) {
            JsonElement ingredient = object.get(INGREDIENT + i);
            if (ingredient == null || ingredient.isJsonNull()) {
                break;
            }
            ingredients[i - 1] = ingredient.getAsString().strip();
            measurements[i - 1] = parse(object.get(MEASURE + i));

            if (measurements[i - 1] != null) {
                measurements[i - 1] = measurements[i - 1].strip();
            }
        }

        return new Drink(id.getAsString(), name.getAsString(), parse(object.get(CATEGORY)), parse(object.get(TYPE)),
            parse(object.get(GLASS)), parse(object.get(INSTRUCTIONS)),
            Arrays.copyOf(ingredients, i - 1), Arrays.copyOf(measurements, i - 1)
        );
    }

    private static String parse(JsonElement element) {
        return element == null || element.isJsonNull() ? null : element.getAsString();
    }
}
