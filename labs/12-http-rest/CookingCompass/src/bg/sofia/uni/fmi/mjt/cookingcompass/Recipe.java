package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.types.CuisineType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DietType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DishType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.HealthType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.MealType;

public record Recipe(String label,
                     DietType[] dietLabels,
                     HealthType[] healthLabels,
                     double totalWeight,
                     CuisineType[] cuisineType,
                     MealType[] mealType,
                     DishType[] dishType,
                     String[] ingredientLines) {
}
