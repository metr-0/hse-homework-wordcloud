package com.example.fileanalysisservice.controller;

import com.example.fileanalysisservice.service.AnalysisService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalysisController.class)
class AnalysisControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalysisService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AnalysisService service() {
            return Mockito.mock(AnalysisService.class);
        }
    }

    @Test
    void analyzeFile() throws Exception {
        UUID fileId = UUID.randomUUID();
        when(service.analyzeFile(fileId)).thenReturn("abc.png");

        mockMvc.perform(post("/analysis/file/{fileId}", fileId))
                .andExpect(status().isOk())
                .andExpect(content().string("abc.png"));
    }

    @Test
    void getImage() throws Exception {
        byte[] image = "img".getBytes();
        ByteArrayResource resource = new ByteArrayResource(image);

        when(service.getImage("abc.png")).thenReturn(resource);

        mockMvc.perform(get("/analysis/cloud/{location}", "abc.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(image));
    }

}
