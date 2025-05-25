package com.example.filestoringservice.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileStorage {

    @Value("${file.storage-location}")
    private String storageDir;

    public String saveFile(MultipartFile file, String filename) throws IOException {
        Path path = Paths.get(storageDir).resolve(filename);
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());
        return path.toAbsolutePath().toString();
    }

    public Resource loadFile(String filePath) {
        return new FileSystemResource(filePath);
    }

}
