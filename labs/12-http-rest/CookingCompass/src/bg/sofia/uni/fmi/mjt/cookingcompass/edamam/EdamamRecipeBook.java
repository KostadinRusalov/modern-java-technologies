package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.Link;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EdamamRecipeBook {
    private List<Collection<Recipe>> pages;
    private Link lastLink;

    public EdamamRecipeBook(Collection<Recipe> firstPage, Link lastLink) {
        this.pages = new ArrayList<>();
        this.pages.add(firstPage);
        this.lastLink = lastLink;
    }

    public void setPages(List<Collection<Recipe>> pages) {
        this.pages = pages;
    }

    public Collection<Recipe> getPage(int page) {
        if (page <= 0) {
            throw new IllegalArgumentException("Page must be positive");
        }

        if (page <= pages.size()) {
            return pages.get(page - 1);
        }

        while (pages.size() < page && lastLink.hasNext()) {
            EdamamResponse response = lastLink.next();
            pages.add(response.getRecipes());
            lastLink = response.link();
        }

        if (pages.size() < page) {
            throw new IllegalArgumentException("Page does not exist");
        }

        return pages.getLast();
    }
}