package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to update user profile information")
public record UserUpdateRequest(
        @Schema(description = "User's updated full name", example = "John Smith")
        @NotBlank String name,
        
        @Schema(description = "User's updated email address", example = "john.smith@example.com")
        @NotBlank @Email String email,
        
        @Schema(description = "Confirmation of the updated email address", example = "john.smith@example.com")
        @NotBlank @Email String emailRepeated,
        
        @Schema(description = "Updated URL for the user's avatar", example = "https://example.com/avatars/jsmith.png")
        String avatarUrl,
        
        @Schema(description = "Updated user profile description", example = "Avid collector and photographer.")
        String description
) {
}
