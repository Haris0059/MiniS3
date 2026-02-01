package com.haris.minis3.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileUpdateRequest {
    private String originalFilename;
    private String tags;
}
