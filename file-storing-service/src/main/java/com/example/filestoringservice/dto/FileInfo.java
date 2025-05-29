package com.example.filestoringservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.Resource;

@Getter
@AllArgsConstructor
public class FileInfo {

    private String originalFilename;
    private Resource resource;

}
