package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to register a new user")
public record RegisterRequest(
        @Schema(description = "User's full name", example = "Jane Doe")
        @NotBlank String name,
        
        @Schema(description = "User's email address", example = "jane.doe@example.com")
        @NotBlank @Email String email,
        
        @Schema(description = "User's password (min 6 characters)", example = "securePass123")
        @NotBlank @Size(min = 6) String password,
        
        @Schema(description = "Confirmation of user's password", example = "securePass123")
        @NotBlank @Size(min = 6) String passwordRepeated
) {
}
