package com.csv.uploader.handler;

import static java.util.logging.Logger.getLogger;

import com.csv.uploader.dto.CSVRecord;
import com.csv.uploader.repository.CSVFileRepository;
import com.csv.uploader.repository.CSVRecordRepository;
import com.csv.uploader.service.FileParseService;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public record CSVRecordHandler(CSVRecordRepository recordRepository,
                               CSVFileRepository fileRepository,
                               FileParseService parseService) {

    final static Logger logger = getLogger("CSVRecordHandler");

    public Flux<CSVRecord> getAllRecords() {
        return recordRepository.findAll();
    }

    public Mono<CSVRecord> getRecord(final String recordId) {
        return recordRepository.findById(recordId);
    }

    public Flux<CSVRecord> parseFile(final String fileId) {
        return parseService.parseRecords(fileRepository.findById(fileId))
            .flatMap(recordRepository::save);
    }

    public Mono<Void> deleteRecord(final String recordId) {
        logger.info("Deleting record with id: " + recordId);
        return recordRepository.deleteById(recordId);
    }
}
