package com.example.filestoringservice.service;

import com.example.filestoringservice.dto.FileInfo;
import com.example.filestoringservice.model.StoredFile;
import com.example.filestoringservice.repository.FileRepository;
import com.example.filestoringservice.storage.FileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public @ExtendWith(MockitoExtension.class)
class FileServiceTests {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private HashService hashService;

    @Mock
    private FileStorage fileStorage;

    @InjectMocks
    private FileService fileService;

    @Test
    void storeFile_shouldSaveNewFile_whenHashNotExists() throws Exception {
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "hello".getBytes());

        String hash = "abc123";
        UUID generatedId = UUID.randomUUID();
        String location = "/tmp/" + generatedId + "_test.txt";

        when(hashService.computeHash(any(InputStream.class))).thenReturn(hash);
        when(fileRepository.findByHash(hash)).thenReturn(Optional.empty());
        when(fileStorage.saveFile(eq(mockFile), anyString())).thenReturn(location);
        when(fileRepository.save(any())).thenAnswer(invocation -> {
            StoredFile sf = invocation.getArgument(0);
            sf.setId(generatedId);
            return sf;
        });

        UUID resultId = fileService.storeFile(mockFile);

        assertEquals(generatedId, resultId);
        verify(fileRepository).save(any());
        verify(fileStorage).saveFile(eq(mockFile), anyString());
    }

    @Test
    void storeFile_shouldReturnExistingId_whenHashExists() throws Exception {
        MultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        String hash = "abc123";
        UUID existingId = UUID.randomUUID();
        StoredFile stored = new StoredFile();
        stored.setId(existingId);
        stored.setHash(hash);

        when(hashService.computeHash(any())).thenReturn(hash);
        when(fileRepository.findByHash(hash)).thenReturn(Optional.of(stored));

        UUID resultId = fileService.storeFile(mockFile);

        assertEquals(existingId, resultId);
        verify(fileRepository, never()).save(any());
    }

    @Test
    void getFile_shouldReturnResource_whenFileExists() {
        UUID id = UUID.randomUUID();
        StoredFile stored = new StoredFile();
        stored.setId(id);
        stored.setLocation("/tmp/file.txt");
        stored.setName("original_name.txt");

        Resource mockResource = mock(Resource.class);

        when(fileRepository.findById(id)).thenReturn(Optional.of(stored));
        when(fileStorage.loadFile(stored.getLocation())).thenReturn(mockResource);

        FileInfo result = fileService.getFile(id);

        assertNotNull(result);
        assertEquals(mockResource, result.getResource());
        assertEquals("original_name.txt", result.getOriginalFilename());
    }

    @Test
    void deleteFile_shouldDeletePhysicalAndDb_whenFileExists() throws Exception {
        UUID id = UUID.randomUUID();
        StoredFile stored = new StoredFile();
        stored.setId(id);
        stored.setLocation("/tmp/file.txt");

        when(fileRepository.findById(id)).thenReturn(Optional.of(stored));

        fileService.deleteFile(id);

        verify(fileStorage).deleteFile(stored.getLocation());
        verify(fileRepository).delete(stored);
    }

    @Test
    void getAllFileIds_shouldReturnListOfIds() {
        StoredFile f1 = new StoredFile();
        f1.setId(UUID.randomUUID());
        StoredFile f2 = new StoredFile();
        f2.setId(UUID.randomUUID());

        when(fileRepository.findAll()).thenReturn(List.of(f1, f2));

        List<UUID> result = fileService.getAllFileIds();

        assertEquals(List.of(f1.getId(), f2.getId()), result);
    }
}
