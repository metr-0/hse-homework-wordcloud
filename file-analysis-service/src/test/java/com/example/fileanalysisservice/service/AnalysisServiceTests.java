package com.example.fileanalysisservice.service;

import com.example.fileanalysisservice.client.FileStoringClient;
import com.example.fileanalysisservice.client.WordCloudApiClient;
import com.example.fileanalysisservice.model.AnalyzedFile;
import com.example.fileanalysisservice.repository.AnalyzedFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalysisServiceTests {

    @Mock
    private AnalyzedFileRepository repository;

    @Mock
    private FileStoringClient fileStoringClient;

    @Mock
    private WordCloudApiClient wordCloudApiClient;

    @InjectMocks
    private AnalysisService service;

    private final UUID fileId = UUID.randomUUID();

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(service, "storageLocation", tempDir.toString());
    }

    @Test
    void analyzeFile_whenAlreadyExists_returnsExistingLocation() {
        String existingLocation = "123.png";
        when(repository.findById(fileId))
                .thenReturn(Optional.of(new AnalyzedFile(fileId, existingLocation)));

        String result = service.analyzeFile(fileId);

        assertEquals(existingLocation, result);
        verify(repository, never()).save(any());
        verify(fileStoringClient, never()).getFile(any());
        verify(wordCloudApiClient, never()).getWordCloud(any());
    }

    @Test
    void analyzeFile_whenNotExists_fetchesAndSavesImage() throws IOException {

        when(repository.findById(fileId)).thenReturn(Optional.empty());

        byte[] fileContent = "some text".getBytes();
        byte[] imageBytes = "fake png".getBytes();

        when(fileStoringClient.getFile(fileId)).thenReturn(fileContent);
        when(wordCloudApiClient.getWordCloud(fileContent)).thenReturn(imageBytes);

        String filename = service.analyzeFile(fileId);

        assertEquals(fileId + ".png", filename);

        Path savedPath = tempDir.resolve(filename);
        assertTrue(Files.exists(savedPath));
        assertArrayEquals(imageBytes, Files.readAllBytes(savedPath));

        verify(repository).save(argThat(f ->
                f.getFileId().equals(fileId) &&
                        f.getLocation().equals(filename)
        ));
    }

    @Test
    void getImage_returnsCorrectResource() throws IOException {
        String location = "test.png";
        Path imagePath = tempDir.resolve(location);
        byte[] content = "image content".getBytes();
        Files.write(imagePath, content);

        ReflectionTestUtils.setField(service, "storageLocation", tempDir.toString());

        Resource resource = service.getImage(location);

        assertTrue(resource.exists());
        assertArrayEquals(content, resource.getInputStream().readAllBytes());
    }

}

