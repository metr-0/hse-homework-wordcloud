package com.example.fileanalysisservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WordCloudApiClient {

    private final WebClient webClient;

    public WordCloudApiClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://quickchart.io").build();
    }

    public byte[] getWordCloud(byte[] fileContent) {
        String text = new String(fileContent); // читаем текст из байтов

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/wordcloud")
                        .queryParam("text", text)
                        .queryParam("format", "png")
                        .queryParam("width", 600)
                        .queryParam("height", 600)
                        .queryParam("fontFamily", "serif")
                        .queryParam("fontWeight", "normal")
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

}
