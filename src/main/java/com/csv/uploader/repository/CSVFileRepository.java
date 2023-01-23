package com.csv.uploader.repository;

import com.csv.uploader.dto.CSVFileData;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface CSVFileRepository extends ReactiveCassandraRepository<CSVFileData, String> {

}
