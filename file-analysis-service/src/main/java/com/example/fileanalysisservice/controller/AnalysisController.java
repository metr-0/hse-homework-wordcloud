package com.example.fileanalysisservice.controller;

import com.example.fileanalysisservice.service.AnalysisService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisService service;

    public AnalysisController(AnalysisService service) {
        this.service = service;
    }

    @PostMapping("/file/{fileId}")
    public ResponseEntity<String> analyzeFile(@PathVariable UUID fileId) {
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
