package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseTable implements Table {
    private final Map<Integer, Column> columnData;
    private final Map<String, Integer> columnNames;

    private int rollsCount;

    public BaseTable() {
        columnData = new LinkedHashMap<>();
        columnNames = new HashMap<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        if (!columnData.isEmpty() && data.length != columnData.keySet().size()) {
            throw new CsvDataNotCorrectException("Data is not the correct format");
        }

        if (columnData.isEmpty()) {
            for (int i = 0; i < data.length; i++) {
                columnNames.put(data[i], i);
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
        return columnNames.keySet();
    }

    @Override
    public Collection<String> getColumnData(String column) {
        return columnData.get(columnNames.get(column)).getData();
    }

    @Override
    public int getRowsCount() {
        return rollsCount;
    }
}
