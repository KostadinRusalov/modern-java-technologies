package bg.sofia.uni.fmi.mjt.ethanolchat.server.models;

import bg.sofia.uni.fmi.mjt.ethanolchat.server.formatting.DrinkFormatter;
import bg.sofia.uni.fmi.mjt.ethanolchat.bots.thecocktaildb.models.Drink;

import java.io.Writer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WishList {
    private Set<Drink> drinks;

    public WishList() {
        this.drinks = new HashSet<>();
    }

    public WishList(Set<Drink> drinks) {
        this.drinks = drinks;
    }

    public Set<Drink> getDrinks() {
        return drinks;
    }

    public boolean add(Drink drink) {
        return drinks.add(drink);
    }

    public boolean remove(Drink drink) {
        return drinks.remove(drink);
    }

    public void saveWishlist(Writer writer, DrinkFormatter drinkFormatter) {
        drinks.forEach(drink -> drinkFormatter.formatDrink(drink, writer));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        WishList wishList = (WishList) object;
        return Objects.equals(drinks, wishList.drinks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drinks);
    }
}