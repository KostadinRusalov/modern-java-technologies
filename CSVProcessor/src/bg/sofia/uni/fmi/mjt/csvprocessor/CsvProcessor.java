package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;

import java.io.Reader;
import java.io.Writer;

public class CsvProcessor implements CsvProcessorAPI {

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {

    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {

    }
}
