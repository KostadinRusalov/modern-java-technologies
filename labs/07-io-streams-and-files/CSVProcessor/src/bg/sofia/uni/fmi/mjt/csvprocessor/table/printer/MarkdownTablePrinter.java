package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkdownTablePrinter implements TablePrinter {
    private static final int DEFAULT_WIDTH = 3;

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        List<String> tableRows = new ArrayList<>();
        int[] widths = getColumnWidths(table);

        tableRows.add(joinColumnNames(table, widths));
        tableRows.add(joinAlignments(widths, alignments));
        tableRows.addAll(joinRows(table, widths));

        return tableRows;
    }

    private String joinColumnNames(Table table, int[] widths) {
        StringBuilder columnNames = new StringBuilder("|");
        int i = 0;
        for (String column : table.getColumnNames()) {
            appendData(columnNames, column, widths[i++]);
        }
        return columnNames.toString();
    }

    private String joinAlignments(int[] widths, ColumnAlignment... alignments) {
        StringBuilder alignmentsBuilder = new StringBuilder("|");
        int minLength = Math.min(widths.length, alignments.length);

        for (int i = 0; i < minLength; ++i) {
            String dashes = "-".repeat(widths[i] - alignments[i].getAlignmentCharactersCount());
            alignmentsBuilder.append(' ');
            alignmentsBuilder.append(switch (alignments[i]) {
                case LEFT -> ":" + dashes;
                case CENTER -> ":" + dashes + ":";
                case RIGHT -> dashes + ":";
                case NOALIGNMENT -> dashes;
            });

            alignmentsBuilder.append(" |");
        }

        if (alignments.length == minLength) {
            for (int i = minLength; i < widths.length; ++i) {
                alignmentsBuilder.append(' ');
                alignmentsBuilder.append("-".repeat(widths[i]));
                alignmentsBuilder.append(" |");
            }
        }

        return alignmentsBuilder.toString();
    }

    private List<String> joinRows(Table table, int[] widths) {
        Map<Integer, List<String>> tableAsMap = getTableAsMap(table);
        List<String> rows = new ArrayList<>(table.getRowsCount());

        for (int i = 0; i < table.getRowsCount() - 1; ++i) {
            StringBuilder rowBuilder = new StringBuilder("|");
            for (int j = 0; j < widths.length; ++j) {
                appendData(rowBuilder, tableAsMap.get(j).get(i), widths[j]);
            }

            rows.add(rowBuilder.toString());
        }

        return rows;
    }

    private void appendData(StringBuilder stringBuilder, String data, int width) {
        stringBuilder.append(' ')
            .append(data)
            .append(" ".repeat(width - data.length() + 1))
            .append('|');
    }

    private Map<Integer, List<String>> getTableAsMap(Table table) {
        Map<Integer, List<String>> tableAsMap = new HashMap<>();
        int i = 0;
        for (String columnName : table.getColumnNames()) {
            tableAsMap.put(i++, new ArrayList<>(table.getColumnData(columnName)));
        }

        return tableAsMap;
    }

    private int[] getColumnWidths(Table table) {
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
}
