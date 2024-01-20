package bg.sofia.uni.fmi.mjt.cookingcompass.types;

import com.google.gson.annotations.SerializedName;

public enum DishType {
    @SerializedName("alcohol cocktail")
    ALCOHOL_COCKTAIL("alcohol cocktail"),

    @SerializedName("biscuits and cookies")
    BISCUITS_AND_COOKIES("biscuits and cookies"),

    @SerializedName("bread")
    BREAD("bread"),

    @SerializedName("cereals")
    CEREALS("cereals"),

    @SerializedName("condiments and sauces")
    CONDIMENTS_AND_SAUCES("condiments and sauces"),

    @SerializedName("desserts")
    DESSERTS("desserts"),

    @SerializedName("drinks")
    DRINKS("drinks"),

    @SerializedName("egg")
    EGG("egg"),

    @SerializedName("ice cream and custard")
    ICE_CREAM_AND_CUSTARD("ice cream and custard"),

    @SerializedName("main course")
    MAIN_COURSE("main course"),

    @SerializedName("pancake")
    PANCAKE("pancake"),

    @SerializedName("pasta")
    PASTA("pasta"),

    @SerializedName("pastry")
    PASTRY("pastry"),

    @SerializedName("pies and tarts")
    PIES_AND_TARTS("pies and tarts"),

    @SerializedName("pizza")
    PIZZA("pizza"),

    @SerializedName("preps")
    PREPS("preps"),

    @SerializedName("preserve")
    PRESERVE("preserve"),

    @SerializedName("salad")
    SALAD("salad"),

    @SerializedName("sandwiches")
    SANDWICHES("sandwiches"),

    @SerializedName("seafood")
    SEAFOOD("seafood"),

    @SerializedName("side dish")
    SIDE_DISH("side dish"),

    @SerializedName("soup")
    SOUP("soup"),

    @SerializedName("special occasions")
    SPECIAL_OCCASIONS("special occasions"),

    @SerializedName("starter")
    STARTER("starter"),

    @SerializedName("sweets")
    SWEETS("sweets");

    private final String dishType;

    DishType(String dishType) {
        this.dishType = dishType;
    }

    @Override
    public String toString() {
        return dishType;
    }
}
