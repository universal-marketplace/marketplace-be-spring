package com.example.universalmarketplacebe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
@Tag(name = "Health Controller", description = "Endpoints for monitoring application health")
public class HealthController {

    @GetMapping("/")
    @Operation(summary = "Root health endpoint", description = "Returns the status of the API service")
    public Map<String, String> rootHealth() {
        return Map.of("status", "UP", "message", "Universal Marketplace API is running");
    }

    @GetMapping("/api/v1/health")
    @Operation(summary = "API v1 health endpoint", description = "Returns the status of the API service")
    public Map<String, String> apiHealth() {
        return Map.of("status", "UP", "message", "Universal Marketplace API v1 is running");
    }
}
