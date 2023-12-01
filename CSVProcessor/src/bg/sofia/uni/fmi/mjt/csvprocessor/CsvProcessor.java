package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.TablePrinter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;

public class CsvProcessor implements CsvProcessorAPI {
    private final Table table;
    private final TablePrinter tablePrinter = new MarkdownTablePrinter();

    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        try (var bufferedReader = new BufferedReader(reader)) {
            String line = bufferedReader.readLine();
            while (line != null) {
                table.addData(line.split(delimiter));
                line = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception during reading", ex);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        try (var bufferedWriter = new BufferedWriter(writer)) {
            var rows = tablePrinter.printTable(table, alignments);
            for (String row : rows) {
                bufferedWriter.write(row);
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception during reading", ex);
        }
    }
}
