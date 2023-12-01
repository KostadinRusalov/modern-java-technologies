package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.TableTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.midi.MidiChannel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TablePrinterTest {

    @Mock
    private Table table;

    private TablePrinter printer;

    @BeforeEach
    public void setUp() {
        printer = new MarkdownTablePrinter();
    }

    @Test
    public void testPrintColumns() {
        when(table.getColumnNames()).thenReturn(List.of("Col 1", "Col 2", "Col 3"));

        assertEquals("| Col 1 | Col 2 | Col 3 |", printer.printTable(table).toArray()[0]);
    }

    @Test
    public void testPrintColumnsAndAlignments() {
        when(table.getColumnNames()).thenReturn(List.of("Col1", "Col 2", "Column 33"));

        assertEquals(List.of(
                "| Col1 | Col 2 | Column 33 |",
                "| :--: | :---- | --------: |"
            ),
            printer.printTable(table, CENTER, LEFT, RIGHT)
        );
    }

    @Test
    public void testPrintTableAllAlignments() {
        when(table.getColumnNames()).thenReturn(List.of("col 1", "col 2", "col 3"));
        when(table.getColumnData(anyString())).thenReturn(List.of("data 1", "data 2", "data 3", "data 4"));
        when(table.getRowsCount()).thenReturn(5);

        assertEquals(List.of(
                "| col 1  | col 2  | col 3  |",
                "| -----: | :----: | :----- |",
                "| data 1 | data 1 | data 1 |",
                "| data 2 | data 2 | data 2 |",
                "| data 3 | data 3 | data 3 |",
                "| data 4 | data 4 | data 4 |"
            ),
            printer.printTable(table, RIGHT, CENTER, LEFT)
        );
    }

    @Test
    public void testPrintTableWithLessAlignments() {
        when(table.getColumnNames()).thenReturn(List.of("n", "first name", "family name", "status"));
        when(table.getColumnData("n")).thenReturn(List.of("1", "2", "3", "4"));
        when(table.getColumnData("first name")).thenReturn(List.of("Satoru", "Izuku", "Luffy", "L"));
        when(table.getColumnData("family name")).thenReturn(List.of("Gojo", "Midoriya", "no info", "-||-"));
        when(table.getColumnData("status")).thenReturn(List.of("sealed", "depressed", "pirate king", "dead"));
        when(table.getRowsCount()).thenReturn(5);

        assertEquals(List.of(
                "| n   | first name | family name | status      |",
                "| :-: | :--------- | ----------- | ----------- |",
                "| 1   | Satoru     | Gojo        | sealed      |",
                "| 2   | Izuku      | Midoriya    | depressed   |",
                "| 3   | Luffy      | no info     | pirate king |",
                "| 4   | L          | -||-        | dead        |"
            ),
            printer.printTable(table, CENTER, LEFT)
        );
    }

    @Test
    public void testPrintTableWithMoreAlignments() {
        when(table.getColumnNames()).thenReturn(List.of("n", "first name", "family name", "status"));
        when(table.getColumnData("n")).thenReturn(List.of("1", "2", "3", "4"));
        when(table.getColumnData("first name")).thenReturn(List.of("Satoru", "Izuku", "Luffy", "L"));
        when(table.getColumnData("family name")).thenReturn(List.of("Gojo", "Midoriya", "no info", "-||-"));
        when(table.getColumnData("status")).thenReturn(List.of("sealed", "depressed", "pirate king", "dead"));
        when(table.getRowsCount()).thenReturn(5);

        assertEquals(List.of(
                "| n   | first name | family name | status      |",
                "| :-: | :--------- | ----------: | :---------: |",
                "| 1   | Satoru     | Gojo        | sealed      |",
                "| 2   | Izuku      | Midoriya    | depressed   |",
                "| 3   | Luffy      | no info     | pirate king |",
                "| 4   | L          | -||-        | dead        |"
            ),
            printer.printTable(table, CENTER, LEFT, RIGHT, CENTER, NOALIGNMENT, LEFT, RIGHT)
        );
    }

}
