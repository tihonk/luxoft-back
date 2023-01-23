package com.csv.uploader.service.impl;

import com.csv.uploader.dto.CSVFileData;
import com.csv.uploader.dto.CSVRecord;
import com.csv.uploader.service.FileParseService;
import com.csv.uploader.service.SuperCSVService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CSVFileParseServiceImpl implements FileParseService {

    private final SuperCSVService csvService;

    CSVFileParseServiceImpl(SuperCSVService csvService) {
        this.csvService = csvService;
    }

    @Override
    public Flux<CSVRecord> parseRecords(final Mono<CSVFileData> csvFileData) {
        return csvFileData.flatMapMany(record -> csvService.prepareRecordsToSave(record.getFilePath()));
    }
}
