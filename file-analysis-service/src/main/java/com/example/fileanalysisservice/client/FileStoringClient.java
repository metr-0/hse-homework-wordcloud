package com.example.fileanalysisservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class FileStoringClient {

    private final WebClient webClient;

    public FileStoringClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://file-storing-service:8001").build();
    }

    public byte[] getFile(UUID fileId) {
        return webClient.get()
                .uri("/files/{id}", fileId)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

}
