package bg.sofia.uni.fmi.mjt.cookingcompass.types;

import com.google.gson.annotations.SerializedName;

public enum MealType {
    @SerializedName("breakfast")
    BREAKFAST("breakfast"),

    @SerializedName("brunch")
    BRUNCH("brunch"),

    @SerializedName("lunch/dinner")
    LUNCH_DINNER("lunch/dinner"),

    @SerializedName("snack")
    SNACK("snack"),

    @SerializedName("teatime")
    TEATIME("teatime");

    private final String mealType;

    MealType(String mealType) {
        this.mealType = mealType;
    }

    @Override
    public String toString() {
        return mealType;
    }
    }
