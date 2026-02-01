package com.haris.minis3.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Setter @Getter
public class FileMetadataDto {
    private UUID id;
    private String originalFilename;
    private long size;
    private Instant uploadDate;
    private String tags;
    private String contentType;
}
