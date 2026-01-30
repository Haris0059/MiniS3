package com.haris.minis3.controller;

import com.haris.minis3.entity.FileMetadata;
import com.haris.minis3.service.FileStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/files")
public class FileController {

    private final FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileStorageService.storeFile(file));
    }

    @GetMapping
    public ResponseEntity<List<FileMetadata>> listFiles() {
        return ResponseEntity.ok(fileStorageService.getAllFiles());
    }
}
