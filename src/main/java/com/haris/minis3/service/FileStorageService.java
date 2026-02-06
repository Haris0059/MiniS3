package com.haris.minis3.service;

import com.haris.minis3.entity.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileStorageService {
    FileMetadata storeFile(MultipartFile file) throws IOException;

    Page<FileMetadata> getAllFiles(Pageable pageable);

    Resource loadFileAsResource(UUID id);

    void deleteFile(UUID id) throws IOException;

    FileMetadata updateFile(UUID id, String originalFilename, String tags);
}
