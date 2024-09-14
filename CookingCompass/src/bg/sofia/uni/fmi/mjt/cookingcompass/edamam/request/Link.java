package bg.sofia.uni.fmi.mjt.cookingcompass.edamam.response;

import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.EdamamClient;
import com.google.gson.annotations.SerializedName;

import java.util.Iterator;

public record Link(@SerializedName("next") Next page) implements Iterator<EdamamResponse> {
    @Override
    public boolean hasNext() {
        return page != null;
    }

    @Override
    public EdamamResponse next() {
        return EdamamClient.getInstance().send(page.href());
    }
}