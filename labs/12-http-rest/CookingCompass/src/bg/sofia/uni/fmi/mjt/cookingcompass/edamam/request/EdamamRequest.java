package bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request;

import bg.sofia.uni.fmi.mjt.cookingcompass.RecipeRequest;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.CuisineType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DietType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DishType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.HealthType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.MealType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.RecipeType;

import java.net.URI;
import java.net.http.HttpRequest;

public class EdamamRequest implements RecipeRequest {

    private final String request;
    private final HttpRequest httpRequest;

    private EdamamRequest(String request) {
        this.request = request;
        this.httpRequest = HttpRequest.newBuilder(URI.create(request)).build();
    }

    public String getRequest() {
        return request;
    }

    public HttpRequest getHttpRequest() {
        return httpRequest;
    }

    public static Builder builder() {
        return builder(RecipeType.PUBLIC);
    }

    public static Builder builder(RecipeType recipeType) {
        return new Builder(recipeType);
    }

    public static class Builder implements RecipeRequest.Builder {

        private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2?type=public";
        private static final String APP_ID = "&app_id=ID";
        private static final String APP_KEY = "&app_key=KEY";
        private static final String TYPE = "&type=";
        private static final String QUERY = "&q=";
        private static final String CUISINE_TYPE = "&cuisineType=";
        private static final String DIET_TYPE = "&diet=";
        private static final String DISH_TYPE = "&dishType=";
        private static final String HEALTH_TYPE = "&health=";
        private static final String MEAL_TYPE = "&mealType=";
        private final StringBuilder requestUrl;

        public Builder() {
            this(RecipeType.PUBLIC);
        }

        public Builder(RecipeType recipeType) {
            requestUrl = new StringBuilder(BASE_URL)
                .append(APP_ID)
                .append(APP_KEY)
                .append(TYPE)
                .append(recipeType);
        }

        @Override
        public EdamamRequest.Builder withQuery(String query, String... queries) {
            requestUrl.append(QUERY).append(query);
            for (String q : queries) {
                requestUrl.append(QUERY).append(q);
            }
            return this;
        }

        @Override
        public EdamamRequest.Builder withCuisines(CuisineType cuisine, CuisineType... cuisines) {
            requestUrl.append(CUISINE_TYPE).append(cuisine);
            for (CuisineType c : cuisines) {
                requestUrl.append(CUISINE_TYPE).append(c);
            }
            return this;
        }

        @Override
        public EdamamRequest.Builder withDiets(DietType diet, DietType... diets) {
            requestUrl.append(DIET_TYPE).append(diet);
            for (DietType d : diets) {
                requestUrl.append(DIET_TYPE).append(d);
            }
            return this;
        }

        @Override
        public EdamamRequest.Builder withDishes(DishType dish, DishType... dishes) {
            requestUrl.append(DISH_TYPE).append(dish);
            for (DishType d : dishes) {
                requestUrl.append(DISH_TYPE).append(d);
            }
            return this;
        }

        @Override
        public EdamamRequest.Builder withHealths(HealthType health, HealthType... healths) {
            requestUrl.append(HEALTH_TYPE).append(health);
            for (HealthType h : healths) {
                requestUrl.append(HEALTH_TYPE).append(h);
            }
            return this;
        }

        @Override
        public EdamamRequest.Builder withMeals(MealType meal, MealType... meals) {
            requestUrl.append(MEAL_TYPE).append(meal);
            for (MealType m : meals) {
                requestUrl.append(MEAL_TYPE).append(m);
            }
            return this;
        }

        @Override
        public EdamamRequest build() {
            return new EdamamRequest(requestUrl.toString());
        }
    }
}
