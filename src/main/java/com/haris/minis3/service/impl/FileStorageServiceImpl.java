package com.haris.minis3.service.impl;

import com.haris.minis3.entity.FileMetadata;
import com.haris.minis3.exception.FileNotFoundException;
import com.haris.minis3.exception.FileValidationException;
import com.haris.minis3.repository.FileMetadataRepository;
import com.haris.minis3.service.FileStorageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final FileMetadataRepository repository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-size}")
    private long maxFileSize;

    @Value("${file.allowed-types}")
    private List<String> allowedTypes;

    public FileStorageServiceImpl(FileMetadataRepository repository) {
        this.repository = repository;
    }

    @Override
    public FileMetadata storeFile(MultipartFile file) throws IOException {
        this.validateFile(file);

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileMetadata metadata = new FileMetadata();
        metadata.setFilename(fileName);
        metadata.setOriginalFilename(file.getOriginalFilename());
        metadata.setSize(file.getSize());
        metadata.setContentType(file.getContentType());
        metadata.setUploadDate(Instant.now());

        return repository.save(metadata);
    }

    @Override
    public Page<FileMetadata> getAllFiles(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @SneakyThrows
    @Override
    public Resource loadFileAsResource(UUID id) {
        FileMetadata metadata = repository.findById(id).orElseThrow(() -> new FileNotFoundException(id));

        Path filePath = Paths.get(uploadDir).resolve(metadata.getFilename()).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found on disk");
        }

        return resource;
    }

    @Override
    public void deleteFile(UUID id) throws IOException {
        FileMetadata metadata = repository.findById(id).orElseThrow(() -> new FileNotFoundException(id));

        Path filePath = Paths.get(uploadDir).resolve(metadata.getFilename()).normalize();

        Files.deleteIfExists(filePath);
        repository.delete(metadata);
    }

    @Override
    public FileMetadata updateFile(UUID id, String originalFilename, String tags) {
        FileMetadata metadata = repository.findById(id).orElseThrow(() -> new FileNotFoundException(id));

        if (originalFilename != null) {
            metadata.setOriginalFilename(originalFilename);
        } if (tags != null) {
            metadata.setTags(tags);
        }

        return repository.save(metadata);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException("File is empty");
        } if (file.getSize() > maxFileSize) {
            throw new FileValidationException("File size exceeds maximum allowed: " + maxFileSize + " bytes");
        } if (!allowedTypes.contains(file.getContentType())) {
            throw new FileValidationException("File type not allowed: " + file.getContentType());
        }
    }
}
