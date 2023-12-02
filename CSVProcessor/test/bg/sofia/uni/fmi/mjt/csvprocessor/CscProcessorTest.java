package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.CENTER;
import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.LEFT;
import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.NOALIGNMENT;
import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CscProcessorTest {

    @Test
    public void testDuplicateColumns() {
        StringReader reader = new StringReader("col 1,col 1,col 1");
        CsvProcessorAPI processor = new CsvProcessor();

        assertThrows(CsvDataNotCorrectException.class,
            () -> processor.readCsv(reader, ","));
    }

    @Test
    public void testProcessorWithInvalidCSV() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[] {"col 1", "col 2"});

        CsvProcessorAPI processor = new CsvProcessor(table);

        StringReader reader = new StringReader("too.much.data.in.here");

        assertThrows(CsvDataNotCorrectException.class, () -> processor.readCsv(reader, "."));
    }

    @Test
    public void testReadCSVInEmptyTable() throws CsvDataNotCorrectException {
        StringReader stringReader = new StringReader("""
            n,first name,family name,feeling
            1,Osamu,Dazai,depressed
            2,Nakahara,Chuuya,lonely
            3,Sir Arthur Conan,Doyle,sleepy""");

        CsvProcessorAPI processor = new CsvProcessor();
        processor.readCsv(stringReader, ",");

        StringWriter stringWriter = new StringWriter();
        processor.writeTable(stringWriter, CENTER, RIGHT, NOALIGNMENT);

        assertEquals("""
                | n   | first name       | family name | feeling   |
                | :-: | ---------------: | ----------- | --------- |
                | 1   | Osamu            | Dazai       | depressed |
                | 2   | Nakahara         | Chuuya      | lonely    |
                | 3   | Sir Arthur Conan | Doyle       | sleepy    |""",
            stringWriter.toString());
    }

    @Test
    public void testReadCSVWithDelimiter() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[] {"name", "age", "gender", "random"});

        CsvProcessorAPI processor = new CsvProcessor(table);

        StringReader stringReader = new StringReader("""
            Koce^20^Male^in pain
            MacbookAir^2^Apple^space gray""");

        processor.readCsv(stringReader, "^");

        StringWriter writer = new StringWriter();
        processor.writeTable(writer, CENTER, CENTER, CENTER, CENTER);

        assertEquals("""
                | name       | age | gender | random     |
                | :--------: | :-: | :----: | :--------: |
                | Koce       | 20  | Male   | in pain    |
                | MacbookAir | 2   | Apple  | space gray |""",
            writer.toString());
    }

    @Test
    public void testWriteTable() {
        Table table = mock(Table.class);
        when(table.getColumnNames()).thenReturn(List.of("n", "first name", "family name", "status"));
        when(table.getColumnData("n")).thenReturn(List.of("1", "2", "3", "4"));
        when(table.getColumnData("first name")).thenReturn(List.of("Satoru", "Moriarty", "Luffy", "L"));
        when(table.getColumnData("family name")).thenReturn(List.of("Gojo", "William", "no info", "-||-"));
        when(table.getColumnData("status")).thenReturn(List.of("sealed", "scheming", "pirate king", "dead"));
        when(table.getRowsCount()).thenReturn(5);

        CsvProcessorAPI processor = new CsvProcessor(table);

        StringWriter stringWriter = new StringWriter();
        processor.writeTable(stringWriter, CENTER, LEFT, RIGHT, CENTER, NOALIGNMENT, LEFT, RIGHT);
        assertEquals("""
                | n   | first name | family name | status      |
                | :-: | :--------- | ----------: | :---------: |
                | 1   | Satoru     | Gojo        | sealed      |
                | 2   | Moriarty   | William     | scheming    |
                | 3   | Luffy      | no info     | pirate king |
                | 4   | L          | -||-        | dead        |""",
            stringWriter.toString()
        );
    }

    @Test
    public void testDotSeparated() throws CsvDataNotCorrectException {
        StringReader reader = new StringReader("""
            dot.separated.bullshit
            red.one.1
            ted.two.2""");
        CsvProcessorAPI processor = new CsvProcessor();
        processor.readCsv(reader, ".");
        StringWriter writer = new StringWriter();
        processor.writeTable(writer);

        assertEquals("""
                | dot | separated | bullshit |
                | --- | --------- | -------- |
                | red | one       | 1        |
                | ted | two       | 2        |""",
            writer.toString()
        );
    }
}
