package com.example.fileanalysisservice.controller;

import com.example.fileanalysisservice.service.WordCloudService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/analysis")
public class WordCloudController {

    private final WordCloudService service;

    public WordCloudController(WordCloudService service) {
        this.service = service;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<String> analyzeFile(@PathVariable UUID fileId) throws IOException {
        String location = service.analyzeFile(fileId);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/cloud/{location}")
    public ResponseEntity<Resource> getImage(@PathVariable String location) {
        Resource file = service.getImage(location);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(file);
    }

}
