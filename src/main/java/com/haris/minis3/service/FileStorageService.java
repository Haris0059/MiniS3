package com.haris.minis3.service;

import com.haris.minis3.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    FileMetadata storeFile(MultipartFile file) throws IOException;
}
