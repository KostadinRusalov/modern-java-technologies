package bg.sofia.uni.fmi.mjt.cookingcompass;

import bg.sofia.uni.fmi.mjt.cookingcompass.types.CuisineType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DietType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.DishType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.HealthType;
import bg.sofia.uni.fmi.mjt.cookingcompass.types.MealType;

public class EdamamRequest implements RecipeRequest {

    private final String request;

    private EdamamRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements RecipeRequest.Builder {

        private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2?type=public";
        private static final String APP_ID = "&app_id=e410fd45";
        private static final String APP_KEY = "&app_key=6d909ebfe83fafeb792e46f6f1a9cd81";
        private static final String QUERY = "&q=";
        private static final String CUISINE_TYPE = "&cuisineType=";
        private static final String DIET_TYPE = "&diet=";
        private static final String DISH_TYPE = "&dishType=";
        private static final String HEALTH_TYPE = "&health=";
        private static final String MEAL_TYPE = "&mealType=";
        private static final String FIELDS =
            "&field=label&field=dietLabels&field=healthLabels&field=totalWeight&field=cuisineType&field=mealType&field=dishType&field=ingredientLines";
        private final StringBuilder requestUrl;

        public Builder() {
            requestUrl = new StringBuilder(BASE_URL)
                .append(APP_ID)
                .append(APP_KEY)
                .append(FIELDS);
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
