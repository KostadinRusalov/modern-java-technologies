package bg.sofia.uni.fmi.mjt.cookingcompass.types;

import com.google.gson.annotations.SerializedName;

public enum CuisineType {
    @SerializedName("american")
    AMERICAN("american"),

    @SerializedName("asian")
    ASIAN("asian"),

    @SerializedName("british")
    BRITISH("british"),

    @SerializedName("caribbean")
    CARIBBEAN("caribbean"),

    @SerializedName("central europe")
    CENTRAL_EUROPE("central europe"),

    @SerializedName("chinese")
    CHINESE("chinese"),

    @SerializedName("eastern europe")
    EASTERN_EUROPE("eastern europe"),

    @SerializedName("french")
    FRENCH("french"),

    @SerializedName("greek")
    GREEK("greek"),

    @SerializedName("indian")
    INDIAN("indian"),

    @SerializedName("italian")
    ITALIAN("italian"),

    @SerializedName("japanese")
    JAPANESE("japanese"),

    @SerializedName("korean")
    KOREAN("korean"),

    @SerializedName("kosher")
    KOSHER("kosher"),

    @SerializedName("mediterranean")
    MEDITERRANEAN("mediterranean"),

    @SerializedName("mexican")
    MEXICAN("mexican"),

    @SerializedName("middle eastern")
    MIDDLE_EASTERN("middle eastern"),

    @SerializedName("nordic")
    NORDIC("nordic"),

    @SerializedName("south american")
    SOUTH_AMERICAN("south american"),

    @SerializedName("south east asian")
    SOUTH_EAST_ASIAN("south east asian"),

    @SerializedName("world")
    WORLD("world");

    private final String cuisineType;

    CuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    @Override
    public String toString() {
        return cuisineType;
    }
    }
