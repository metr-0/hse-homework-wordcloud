package com.example.apigateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.RouteLocator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GatewayRoutesConfigTests {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    public void routesShouldBeLoaded() {
        var routes = routeLocator.getRoutes().collectList().block();
        assertThat(routes).isNotNull();
        assertThat(routes)
                .anyMatch(r -> r.getId().equals("upload-file"))
                .anyMatch(r -> r.getId().equals("get-file"))
                .anyMatch(r -> r.getId().equals("get-all-files"))
                .anyMatch(r -> r.getId().equals("delete-file"))
                .anyMatch(r -> r.getId().equals("analyze-file"))
                .anyMatch(r -> r.getId().equals("word-cloud"));
    }

}
