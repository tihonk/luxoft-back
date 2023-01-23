package com.csv.uploader.service;

import com.csv.uploader.dto.CSVRecord;
import reactor.core.publisher.Flux;

public interface SuperCSVService {

    Flux<CSVRecord> prepareRecordsToSave(String filePath);
}
