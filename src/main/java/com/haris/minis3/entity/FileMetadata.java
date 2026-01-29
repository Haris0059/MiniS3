package com.haris.minis3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Entity @Getter @Setter
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String filename;
    private String originalFilename;
    private Long size;
    private String contentType;
    private Instant uploadDate;
}
