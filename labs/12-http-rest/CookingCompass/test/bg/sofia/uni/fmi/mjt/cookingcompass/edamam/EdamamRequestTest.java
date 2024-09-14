package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.EdamamRequest;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.CuisineType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DietType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DishType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.HealthType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.MealType;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EdamamRequestTest {
    @Test
    public void testEdamamRequestBuilder() {
        String url = "https://api.edamam.com/api/recipes/v2?type=public&app_id=e410fd45&app_key=6d909ebfe83fafeb" +
            "792e46f6f1a9cd81&type=public&q=chilli&q=chicken&q=lime&q=coconut&mealType=lunch-dinner&mealType=break" +
            "fast&mealType=teatime&diet=low-carb&diet=high-protein&diet=low-fat&health=dairy-free&health=kosher&h" +
            "ealth=pork-free&health=egg-free&dishType=main-course&dishType=desserts&dishType=side-dish&cuisineType=" +
            "south-east-asian&cuisineType=italian&cuisineType=mediterranean";

        EdamamRequest request = EdamamRequest
            .builder()
            .withQuery("chilli", "chicken", "lime", "coconut")
            .withMeals(MealType.LUNCH_DINNER, MealType.BREAKFAST, MealType.TEATIME)
            .withDiets(DietType.LOW_CARB, DietType.HIGH_PROTEIN, DietType.LOW_FAT)
            .withHealths(HealthType.DAIRY_FREE, HealthType.KOSHER, HealthType.PORK_FREE, HealthType.EGG_FREE)
            .withDishes(DishType.MAIN_COURSE, DishType.DESSERTS, DishType.SIDE_DISH)
            .withCuisines(CuisineType.SOUTH_EAST_ASIAN, CuisineType.ITALIAN, CuisineType.MEDITERRANEAN)
            .build();

        assertEquals(url, request.getRequest());

        assertEquals(HttpRequest.newBuilder().uri(request.getHttpRequest().uri()).build(), request.getHttpRequest());
    }

}
