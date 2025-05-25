package com.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @GetMapping("/health")
    public String health() {
        return "API Gateway is very much alive. Thank you for your concern. " +
                "Take a pie, a cup of rice and a cat wife from the shelf!";
    }

}
