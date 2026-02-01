package com.haris.minis3.mapper;

import com.haris.minis3.dto.FileMetadataDto;
import com.haris.minis3.entity.FileMetadata;
import org.springframework.stereotype.Component;

@Component
public class FileMetadataMapper {

    public FileMetadataDto toDto(FileMetadata entity) {
        FileMetadataDto dto = new FileMetadataDto();

        dto.setId(entity.getId());
        dto.setOriginalFilename(entity.getOriginalFilename());
        dto.setSize(entity.getSize());
        dto.setUploadDate(entity.getUploadDate());
        dto.setContentType(entity.getContentType());
        dto.setTags(entity.getTags());

        return dto;
    }
}
