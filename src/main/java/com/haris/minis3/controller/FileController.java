package com.haris.minis3.controller;

import com.haris.minis3.dto.FileMetadataDto;
import com.haris.minis3.dto.FileUpdateRequest;
import com.haris.minis3.entity.FileMetadata;
import com.haris.minis3.mapper.FileMetadataMapper;
import com.haris.minis3.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/files")
@CrossOrigin
public class FileController {

    private final FileStorageService fileStorageService;
    private final FileMetadataMapper fileMetadataMapper;

    public FileController(FileStorageService fileStorageService, FileMetadataMapper fileMetadataMapper) {
        this.fileStorageService = fileStorageService;
        this.fileMetadataMapper = fileMetadataMapper;
    }

    @PostMapping
    public ResponseEntity<FileMetadataDto> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileMetadataMapper.toDto(fileStorageService.storeFile(file)));
    }

    @GetMapping
    public ResponseEntity<List<FileMetadataDto>> listFiles() {
        return ResponseEntity.ok(fileStorageService.getAllFiles()
                .stream()
                .map(fileMetadataMapper::toDto)
                .toList());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID id) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID id) throws IOException {
        fileStorageService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<FileMetadataDto> updateFile(@PathVariable UUID id, @RequestBody FileUpdateRequest request) {
        FileMetadata updated = fileStorageService.updateFile(id, request.getOriginalFilename(), request.getTags());

        return ResponseEntity.ok(fileMetadataMapper.toDto(updated));
    }
}
