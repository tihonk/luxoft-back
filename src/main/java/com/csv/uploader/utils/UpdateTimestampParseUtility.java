package com.csv.uploader.utils;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class UpdateTimestampParseUtility extends CellProcessorAdaptor implements StringCellProcessor {

    private final String value;

    public UpdateTimestampParseUtility(final String value) {
        this.value = value;
    }

    @Override
    public <T> T execute(Object value, CsvContext csvContext) {
        this.validateInputNotNull(value, csvContext);
        if (value instanceof String) {
            if (this.value.equals(value) || "".equals(value)) {
                return this.next.execute(value, csvContext);
            }
        }
        throw new SuperCsvCellProcessorException("the input value should be " + value, csvContext, this);
    }
}
