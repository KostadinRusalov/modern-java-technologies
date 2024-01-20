package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.types.CuisineType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DietType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DishType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.HealthType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.MealType;

public interface RecipeRequest {

    interface Builder {
        Builder withQuery(String query, String... queries);

        Builder withCuisines(CuisineType cuisine, CuisineType... cuisines);

        Builder withDiets(DietType diet, DietType... diets);

        Builder withDishes(DishType dish, DishType... dishes);

        Builder withHealths(HealthType health, HealthType... healths);

        Builder withMeals(MealType meal, MealType... meals);

        RecipeRequest build();
    }
}
