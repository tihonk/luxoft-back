package com.csv.uploader.repository;

import com.csv.uploader.dto.CSVRecord;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface CSVRecordRepository extends ReactiveCassandraRepository<CSVRecord, String> {

}
