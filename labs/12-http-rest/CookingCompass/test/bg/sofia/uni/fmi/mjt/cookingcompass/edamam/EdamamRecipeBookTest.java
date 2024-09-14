package bg.sofia.uni.fmi.mjt.cookingcompass.edamam;

import bg.sofia.uni.fmi.mjt.cookingcompass.Recipe;
import bg.sofia.uni.fmi.mjt.cookingcompass.edamam.request.Link;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EdamamRecipeBookTest {
    @Mock
    private Link link;

    @InjectMocks
    private EdamamRecipeBook edamamRecipeBook;

    private static Recipe recipe;

    @BeforeAll
    public static void setUp() {
        recipe = new Recipe("recipe1", null, null, 10, null, null, null, null);
    }

    @Test
    public void testGetNegativePage() {
        assertThrows(IllegalArgumentException.class, () -> edamamRecipeBook.getPage(-1),
            "Getting page when page is negative should throw IllegalArgumentException");
    }


    @Test
    public void testGetPageThatDoesNotExist() {
        when(link.hasNext()).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> edamamRecipeBook.getPage(2),
            "Getting page that does not exist should throw IllegalArgumentException");
    }

    @Test
    public void testGetPageThatHasBeenCached() {
        Recipe recipe = new Recipe("recipe1", null, null, 10, null, null, null, null);

        List<Collection<Recipe>> pages = mock(List.class);
        when(pages.size()).thenReturn(4);
        when(pages.get(2)).thenReturn(List.of(recipe));

        edamamRecipeBook.setPages(pages);
        assertEquals(List.of(recipe), edamamRecipeBook.getPage(3),
            "Getting page that has been cached should return the page");
    }

    @Test
    public void testGetPageThatHasNotBeenCached() {
        EdamamResponse edamamResponse = mock(EdamamResponse.class);
        Link nextLink = mock(Link.class);
        List<Collection<Recipe>> pages = new ArrayList<>();
        pages.add(List.of(recipe));

        when(link.hasNext()).thenReturn(true);
        when(link.next()).thenReturn(edamamResponse);

        when(edamamResponse.getRecipes()).thenReturn(List.of(recipe));
        when(edamamResponse.link()).thenReturn(nextLink);

        edamamRecipeBook.setPages(pages);

        assertEquals(List.of(recipe), edamamRecipeBook.getPage(2),
            "Getting page that has not been cached should return the page");

    }
}
