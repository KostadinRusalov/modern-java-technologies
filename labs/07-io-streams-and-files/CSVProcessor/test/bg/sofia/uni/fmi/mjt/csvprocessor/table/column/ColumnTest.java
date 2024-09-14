package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ColumnTest {

    @Test
    public void testAddNullData() {
        assertThrows(IllegalArgumentException.class, () -> new BaseColumn().addData(null),
            "Adding null data should throw an exception.");
    }

    @Test
    public void testAddBlankData() {
        assertThrows(IllegalArgumentException.class, () -> new BaseColumn().addData(" "),
            "Adding null data should throw an exception.");
    }

    @Test
    public void testGetData() {
        Column column = new BaseColumn(
            new LinkedHashSet<>(List.of("Dead", "Nanami", "Nobara", "Jogo", "and many more"))
        );

        assertEquals(List.of("Dead", "Nanami", "Nobara", "Jogo", "and many more"), column.getData());
    }

    @Test
    public void testGetDataIsUnmodifiable() {
        Column column = new BaseColumn(
            new LinkedHashSet<>(List.of("Alive", "Yuji", "Sukuna", "Megumi", "Yuta"))
        );

        assertThrows(UnsupportedOperationException.class, () -> column.getData().add("Gojo"),
            "You cannot resurrect the dead.");
    }

    @Test
    public void testAddValidData() {
        Column column = new BaseColumn(new LinkedHashSet<>(List.of("Curses")));
        column.addData("Mahito");
        column.addData("Sukuna");
        column.addData("Love");

        assertEquals(List.of("Curses", "Mahito", "Sukuna", "Love"), column.getData());
    }

}
