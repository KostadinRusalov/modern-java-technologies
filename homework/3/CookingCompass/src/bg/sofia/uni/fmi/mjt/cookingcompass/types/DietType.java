package bg.sofia.uni.fmi.mjt.cookingcompass.types;

import com.google.gson.annotations.SerializedName;

public enum DietType {
    @SerializedName("Balanced")
    BALANCED("balanced"),

    @SerializedName("High-Fiber")
    HIGH_FIBER("high-fiber"),

    @SerializedName("High-Protein")
    HIGH_PROTEIN("high-protein"),

    @SerializedName("Low-Carb")
    LOW_CARB("low-carb"),

    @SerializedName("Low-Fat")
    LOW_FAT("low-fat"),

    @SerializedName("Low-Sodium")
    LOW_SODIUM("low-sodium");

    private final String dietType;

    DietType(String dietType) {
        this.dietType = dietType;
    }

    @Override
    public String toString() {
        return dietType;
    }
}
