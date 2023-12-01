package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvProcessor implements CsvProcessorAPI {
    private static final int DEFAULT_WIDTH = 3;
    private Table table;

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
        int[] widths = getColumnWidths();
        try (var bufferedWriter = new BufferedWriter(writer)) {
            writeColumnNames(bufferedWriter, widths);
            writeAlignments(bufferedWriter, widths, alignments);
            writeRows(bufferedWriter, widths);
        } catch (IOException ex) {
            throw new UncheckedIOException("Exception during reading", ex);
        }
    }

    private int[] getColumnWidths() {
        int[] widths = new int[table.getColumnNames().size()];
        int i = 0;

        for (String columnName : table.getColumnNames()) {
            widths[i] = Math.max(DEFAULT_WIDTH, columnName.length());

            for (String data : table.getColumnData(columnName)) {
                widths[i] = Math.max(widths[i], data.length());
            }
            i++;
        }

        return widths;
    }

    private void writeColumnNames(Writer writer, int[] widths) throws IOException {
        int i = 0;
        writer.write('|');
        for (String columnName : table.getColumnNames()) {
            writeData(writer, columnName, widths[i++]);
        }
    }

    private void writeAlignments(Writer writer, int[] widths, ColumnAlignment... alignments) throws IOException {
        int bound = Math.min(widths.length, alignments.length);
        writer.write('|');
        for (int i = 0; i < bound; ++i) {
            writer.write(switch (alignments[i]) {
                case LEFT -> ":" + "-".repeat(widths[i] - 1);
                case CENTER -> ":" + "-".repeat(widths[i] - 2) + ":";
                case RIGHT -> "-".repeat(widths[i] - 1) + ":";
                case NOALIGNMENT -> "-".repeat(widths[i]);
            });
            writer.write(" |");
        }

        if (alignments.length == bound) {
            for (int i = bound; i < widths.length; ++i) {
                writer.write("-".repeat(widths[i]));
            }
        }
    }

    private void writeRows(Writer writer, int[] widths) throws IOException {
        var tableAsMap = getTableAsMap();
        for (int i = 0; i < table.getRowsCount(); ++i) {
            writer.write('|');
            for (int j = 0; j < table.getColumnNames().size(); ++i) {
                writeData(writer, tableAsMap.get(j).get(i), widths[j]);
            }
        }
    }

    private void writeData(Writer writer, String data, int width) throws IOException {
        writer.write(' ');
        writer.write(data);
        writer.write(" ".repeat(width - data.length() + 1));
        writer.write('|');
    }

    private Map<Integer, List<String>> getTableAsMap() {
        Map<Integer, List<String>> tableAsMap = new HashMap<>();
        int i = 0;
        for (String columnName : table.getColumnNames()) {
            tableAsMap.put(i++, new ArrayList<>(table.getColumnData(columnName)));
        }
        return tableAsMap;
    }

}
