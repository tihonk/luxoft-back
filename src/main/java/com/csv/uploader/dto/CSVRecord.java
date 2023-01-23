package com.csv.uploader.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Getter
@Setter
@Table
public class CSVRecord {

    @PrimaryKey
    private String PRIMARY_KEY;
    private String NAME;
    private String DESCRIPTION;
    private String UPDATED_TIMESTAMP;
}
