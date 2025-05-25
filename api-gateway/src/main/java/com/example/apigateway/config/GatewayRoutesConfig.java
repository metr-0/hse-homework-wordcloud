package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("upload-file", r -> r
                        .path("/files")
                        .and().method("POST")
                        .uri("http://file-storing-service:8081"))

                .route("get-file", r -> r
                        .path("/files/{id}")
                        .and().method("GET")
                        .uri("http://file-storing-service:8081"))

                .route("analyze-file", r -> r
                        .path("analysis/file/{fileId}")
                        .and().method("POST")
                        .uri("http://file-analysis-service:8082"))

                .route("word-cloud", r -> r
                        .path("/analysis/cloud/{location}")
                        .and().method("GET")
                        .uri("http://file-analysis-service:8082"))

                .build();
    }

}
