package com.csv.uploader.service.impl;

import static java.util.logging.Logger.getLogger;
import static org.supercsv.prefs.CsvPreference.STANDARD_PREFERENCE;

import com.csv.uploader.dto.CSVRecord;
import com.csv.uploader.service.SuperCSVService;
import com.csv.uploader.utils.UpdateTimestampParseUtility;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import reactor.core.publisher.Flux;

@Service
public class SuperCSVServiceImpl implements SuperCSVService {

    final static Logger logger = getLogger("SuperCSVServiceImpl");

    private static final String VALID_TIMESTAMP = "ISO8601";

    @Override
    public Flux<CSVRecord> prepareRecordsToSave(final String filePath) {
        try {
            final ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(filePath), STANDARD_PREFERENCE);
            return findRecords(beanReader);
        } catch (IOException e) {
            logger.warning("Failed to get data from file: " + filePath);
            return Flux.empty();
        }
    }

    private Flux<CSVRecord> findRecords(final ICsvBeanReader beanReader) throws IOException {
        final String[] headers = beanReader.getHeader(true);
        final CellProcessor[] processors = getProcessors();
        return findRecords(headers, processors, beanReader);
    }

    private Flux<CSVRecord> findRecords(final String[] headers,
                                        final CellProcessor[] processors,
                                        final ICsvBeanReader beanReader) throws IOException {
        CSVRecord record;
        Flux<CSVRecord> records = Flux.empty();
        while ((record = beanReader.read(CSVRecord.class, headers, processors)) != null) {
            records = Flux.concat(records, Flux.just(record));
        }
        return records;
    }

    private static CellProcessor[] getProcessors() {
        return new CellProcessor[] {
            new NotNull(),           // PRIMARY_KEY
            new Optional(),          // NAME
            new Optional(),          // DESCRIPTION
            new Optional(new UpdateTimestampParseUtility(VALID_TIMESTAMP)) // UPDATED_TIMESTAMP
        };
    }
}
