package bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models;

public record Drink(String id, String name,
                    String category, String type,
                    String glass, String instructions,
                    String[] ingredients, String[] measurements) {
}
