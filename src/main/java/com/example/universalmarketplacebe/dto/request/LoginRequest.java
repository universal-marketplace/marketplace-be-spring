package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request for user authentication")
public record LoginRequest(
        @Schema(description = "User's email address", example = "user@example.com")
        @NotBlank @Email String email,
        
        @Schema(description = "User's password", example = "password123")
        @NotBlank String password
) {
}
