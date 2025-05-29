package com.example.filestoringservice.controller;

import com.example.filestoringservice.dto.FileInfo;
import com.example.filestoringservice.service.FileService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileService fileService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public FileService fileService() {
            return Mockito.mock(FileService.class);
        }
    }

    @Test
    void uploadFile_shouldReturnCreatedAndId() throws Exception {
        UUID fileId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());

        when(fileService.storeFile(any())).thenReturn(fileId);

        mockMvc.perform(multipart("/files").file(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(fileId.toString()));
    }

    @Test
    void getFile_shouldReturnBinaryFile() throws Exception {
        UUID fileId = UUID.randomUUID();
        byte[] fileContent = "hello".getBytes();
        Resource resource = new ByteArrayResource(fileContent);
        String originalFilename = "test_file.txt";

        FileInfo fileInfo = new FileInfo(originalFilename, resource);

        when(fileService.getFile(fileId)).thenReturn(fileInfo);

        mockMvc.perform(get("/files/{id}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(content().bytes(fileContent))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"test_file.txt\""));
    }

    @Test
    void getAllIds_shouldReturnJsonArray() throws Exception {
        List<UUID> ids = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(fileService.getAllFileIds()).thenReturn(ids);

        mockMvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(ids.size()));
    }

    @Test
    void deleteFile_shouldReturnNoContent() throws Exception {
        UUID fileId = UUID.randomUUID();

        mockMvc.perform(delete("/files/{id}", fileId))
                .andExpect(status().isNoContent());

        verify(fileService).deleteFile(fileId);
    }
}
