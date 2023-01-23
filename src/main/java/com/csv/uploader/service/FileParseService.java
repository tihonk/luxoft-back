package com.csv.uploader.service;

import com.csv.uploader.dto.CSVFileData;
import com.csv.uploader.dto.CSVRecord;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileParseService {

    Flux<CSVRecord> parseRecords(Mono<CSVFileData> filePath);
}
