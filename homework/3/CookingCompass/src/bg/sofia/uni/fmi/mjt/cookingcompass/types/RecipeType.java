package bg.sofia.uni.fmi.mjt.cookingcompass.types;

public enum RecipeType {
    PUBLIC("public"),

    User("user"),

    ANY("any");

    private final String type;

    RecipeType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
