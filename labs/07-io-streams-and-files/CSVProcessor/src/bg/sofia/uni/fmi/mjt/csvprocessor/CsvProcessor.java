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
                table.addData(line.split("\\Q" + delimiter + "\\E"));
                line = bufferedReader.readLine();
            }
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception during reading", ex);
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        try (var bufferedWriter = new BufferedWriter(writer)) {
            String[] rows = tablePrinter.printTable(table, alignments).toArray(String[]::new);
            for (int i = 0; i < rows.length - 1; ++i) {
                bufferedWriter.write(rows[i]);
                bufferedWriter.write(System.lineSeparator());
            }
            bufferedWriter.write(rows[rows.length - 1]);
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception during reading", ex);
        }
    }
}
