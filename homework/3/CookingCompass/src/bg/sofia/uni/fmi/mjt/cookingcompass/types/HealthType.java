package bg.sofia.uni.fmi.mjt.cookingcompass.types;

import com.google.gson.annotations.SerializedName;

public enum HealthType {
    @SerializedName("Alcohol-Cocktail")
    ALCOHOL_COCKTAIL("alcohol-cocktail"),

    @SerializedName("Alcohol-Free")
    ALCOHOL_FREE("alcohol-free"),

    @SerializedName("Celery-Free")
    CELERY_FREE("celery-free"),

    @SerializedName("Crustacean-Free")
    CRUSTACEAN_FREE("crustacean-free"),

    @SerializedName("Dairy-Free")
    DAIRY_FREE("dairy-free"),

    @SerializedName("DASH")
    DASH("DASH"),

    @SerializedName("Egg-Free")
    EGG_FREE("egg-free"),

    @SerializedName("Fish-Free")
    FISH_FREE("fish-free"),

    @SerializedName("FODMAP-Free")
    FODMAP_FREE("fodmap-free"),

    @SerializedName("Gluten-Free")
    GLUTEN_FREE("gluten-free"),

    @SerializedName("Immuno-Supportive")
    IMMUNO_SUPPORTIVE("immuno-supportive"),

    @SerializedName("Keto-Friendly")
    KETO_FRIENDLY("keto-friendly"),

    @SerializedName("Kidney-Friendly")
    KIDNEY_FRIENDLY("kidney-friendly"),

    @SerializedName("Kosher")
    KOSHER("kosher"),

    @SerializedName("Low Potassium")
    LOW_POTASSIUM("low-potassium"),

    @SerializedName("Low Sugar")
    LOW_SUGAR("low-sugar"),

    @SerializedName("Lupine-Free")
    LUPINE_FREE("lupine-free"),

    @SerializedName("Mediterranean")
    MEDITERRANEAN("Mediterranean"),

    @SerializedName("Mollusk-Free")
    MOLLUSK_FREE("mollusk-free"),

    @SerializedName("Mustard-Free")
    MUSTARD_FREE("mustard-free"),

    @SerializedName("No oil added")
    NO_OIL_ADDED("No-oil-added"),

    @SerializedName("Paleo")
    PALEO("paleo"),

    @SerializedName("Peanut-Free")
    PEANUT_FREE("peanut-free"),

    @SerializedName("Pescatarian")
    PECATARIAN("pecatarian"),

    @SerializedName("Pork-Free")
    PORK_FREE("pork-free"),

    @SerializedName("Red-Meat-Free")
    RED_MEAT_FREE("red-meat-free"),

    @SerializedName("Sesame-Free")
    SESAME_FREE("sesame-free"),

    @SerializedName("Shellfish-Free")
    SHELLFISH_FREE("shellfish-free"),

    @SerializedName("Soy-Free")
    SOY_FREE("soy-free"),

    @SerializedName("Sugar-Conscious")
    SUGAR_CONSCIOUS("sugar-conscious"),

    @SerializedName("Sulfite-Free")
    SULFITE_FREE("sulfite-free"),

    @SerializedName("Tree-Nut-Free")
    TREE_NUT_FREE("tree-nut-free"),

    @SerializedName("Vegan")
    VEGAN("vegan"),

    @SerializedName("Vegetarian")
    VEGETARIAN("vegetarian"),

    @SerializedName("Wheat-Free")
    WHEAT_FREE("wheat-free");

    private final String healthType;

    HealthType(String healthType) {
        this.healthType = healthType;
    }

    @Override
    public String toString() {
        return healthType;
    }
}
