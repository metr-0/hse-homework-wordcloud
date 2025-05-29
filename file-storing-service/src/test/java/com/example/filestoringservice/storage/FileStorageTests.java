package com.example.filestoringservice.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageTests {

    private FileStorage fileStorage;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileStorage = new FileStorage();
        ReflectionTestUtils.setField(fileStorage, "storageDir", tempDir.toString());
    }

    @Test
    void saveAndDeleteFile_success() throws IOException {
        MultipartFile mockFile = new MockMultipartFile("file", "example.txt",
                "text/plain", "test content".getBytes());

        String filename = "example.txt";
        String savedPath = fileStorage.saveFile(mockFile, filename);

        Path path = Paths.get(savedPath);
        assertTrue(Files.exists(path));
        fileStorage.deleteFile(savedPath);
        assertFalse(Files.exists(path));
    }

    @Test
    void loadFile_returnsCorrectResource() throws IOException {
        Path filePath = tempDir.resolve("test.txt");
        Files.writeString(filePath, "some content");

        Resource resource = fileStorage.loadFile(filePath.toString());

        assertTrue(resource.exists());
        assertEquals("test.txt", resource.getFilename());
    }

}
