package com.example.fileanalysisservice.service;

import com.example.fileanalysisservice.client.FileStoringClient;
import com.example.fileanalysisservice.client.WordCloudApiClient;
import com.example.fileanalysisservice.model.AnalyzedFile;
import com.example.fileanalysisservice.repository.AnalyzedFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class AnalysisService {

    private final AnalyzedFileRepository repository;
    private final FileStoringClient fileStoringClient;
    private final WordCloudApiClient wordCloudApiClient;

    @Value("${file.storage-location}")
    private String storageLocation;

    public AnalysisService(AnalyzedFileRepository repository,
                           FileStoringClient fileStoringClient,
                           WordCloudApiClient wordCloudApiClient) {
        this.repository = repository;
        this.fileStoringClient = fileStoringClient;
        this.wordCloudApiClient = wordCloudApiClient;
    }

    public String analyzeFile(UUID fileId) {
        return repository.findById(fileId)
                .map(AnalyzedFile::getLocation)
                .orElseGet(() -> {
                    byte[] file = fileStoringClient.getFile(fileId);
                    byte[] image = wordCloudApiClient.getWordCloud(file);
                    String filename = fileId + ".png";
                    Path path = Paths.get(storageLocation, filename);

                    try {
                        Files.createDirectories(path.getParent());
                        Files.write(path, image);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image", e);
                    }

                    repository.save(new AnalyzedFile(fileId, filename));
                    return filename;
                });
    }

    public Resource getImage(String location) {
        Path path = Paths.get(storageLocation, location);
        return new FileSystemResource(path);
    }

}
