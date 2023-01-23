package com.csv.uploader.dto;

import lombok.Getter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Table
public class CSVFileData {

    @PrimaryKey
    private final String id;
    private final String filePath;
    private final String fileName;

    public CSVFileData(final String id, final String filePath, final String fileName) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
    }
}
