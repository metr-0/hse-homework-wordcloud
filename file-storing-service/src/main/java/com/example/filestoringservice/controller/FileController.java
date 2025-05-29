package com.example.filestoringservice.controller;

import com.example.filestoringservice.dto.FileInfo;
import com.example.filestoringservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<Map<String, UUID>> uploadFile(@RequestParam("file") MultipartFile file) {
        UUID fileId = fileService.storeFile(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", fileId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable UUID id) {
        FileInfo fileInfo = fileService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileInfo.getOriginalFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileInfo.getResource());
    }

    @GetMapping
    public ResponseEntity<List<UUID>> listAllFileIds() {
        List<UUID> ids = fileService.getAllFileIds();
        return ResponseEntity.ok(ids);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable UUID id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }

}
