package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableTest {
    @Test
    public void testAddNullData() {
        assertThrows(IllegalArgumentException.class, () -> new BaseTable().addData(null),
            "Adding null data should throw an exception");
    }

    @Test
    public void testAddGetColumns() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        String[] columns = new String[] {"Zenin", "Gojo", "Curses", "Fushiguro"};

        table.addData(columns);

        assertEquals(List.of(columns), table.getColumnNames());
    }

    @Test
    public void testAddMoreData() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[] {"Column 1", "Column 2"});

        assertThrows(CsvDataNotCorrectException.class,
            () -> table.addData(new String[] {"One", "Two", "Three", "Number one si ti"}));
    }

    @Test
    public void testAddLessData() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[] {"Column 1", "Column 2", "Column 3"});

        assertThrows(CsvDataNotCorrectException.class,
            () -> table.addData(new String[] {"One", "Two"}));
    }

    @Test
    public void testAddingNoData() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[0]);

        assertEquals(List.of(), table.getColumnNames());
        assertEquals(0, table.getRowsCount());
    }

    @Test
    public void testGetRows() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[] {"Last name", "First name", "Status"});
        table.addData(new String[] {"Fushiguro", "Megumi", "Alive"});
        table.addData(new String[] {"Gojo", "Satoru", "Sealed"});
        table.addData(new String[] {"Itadori", "Yuji", "Heartbroken"});
        table.addData(new String[] {"Kugisaki", "Nobara", "Killed"});

        assertEquals(5, table.getRowsCount());
    }
}
