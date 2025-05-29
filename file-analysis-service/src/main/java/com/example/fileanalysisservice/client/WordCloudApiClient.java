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
        String text = new String(fileContent);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/wordcloud")
                        .queryParam("text", text)
                        .queryParam("format", "png")
                        .queryParam("width", 600)
                        .queryParam("height", 600)
                        .queryParam("rotation", 0)
                        .queryParam("fontFamily", "sans-serif")
                        .queryParam("fontWeight", "semibold")
                        .queryParam("colors",
                                "[\"#1f0006\", \"#1f0006\", \"#8b0000\", \"#7c0015\", \"#b90e0a\"]")
                        .queryParam("backgroundColor", "#eae4e5")
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

}
