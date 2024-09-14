package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseTable implements Table {
    private final Map<Integer, Column> columnData;
    private final Map<String, Integer> columnNames;

    private int rollsCount;

    public BaseTable() {
        columnData = new LinkedHashMap<>();
        columnNames = new LinkedHashMap<>();
        rollsCount = 0;
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (data.length == 0) {
            return;
        }

        if (!columnData.isEmpty() && data.length != columnData.keySet().size()) {
            throw new CsvDataNotCorrectException("Data is not the correct format");
        }

        if (columnData.isEmpty()) {
            for (int i = 0; i < data.length; i++) {
                if (columnNames.containsKey(data[i])) {
                    throw new CsvDataNotCorrectException("Cannot have duplicate columns");
                }

                columnNames.put(data[i], i);
                columnData.put(i, new BaseColumn());
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                columnData.get(i).addData(data[i]);
            }
        }
        rollsCount++;
    }

    @Override
    public Collection<String> getColumnNames() {
        return List.copyOf(columnNames.keySet());
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("Column cannot be null or blank");
        }

        if (columnNames.get(column) == null) {
            throw new IllegalArgumentException("There is no such column");
        }

        return columnData.get(columnNames.get(column)).getData();
    }

    @Override
    public int getRowsCount() {
        return rollsCount;
    }
}
