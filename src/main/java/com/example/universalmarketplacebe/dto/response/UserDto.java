package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object representing a user profile")
public record UserDto(
        @Schema(description = "Unique identifier of the user", example = "1")
        Long id,
        @Schema(description = "Full name of the user", example = "John Doe")
        String name,
        @Schema(description = "Email address of the user", example = "john.doe@example.com")
        String email,
        @Schema(description = "URL of the user's avatar", example = "https://example.com/avatar.jpg")
        String avatarUrl,
        @Schema(description = "Short description or bio of the user", example = "Passionate seller of vintage items.")
        String description,
        @Schema(description = "Average rating of the user", example = "4.5")
        Double rating,
        @Schema(description = "Total number of reviews received by the user", example = "10")
        Integer reviewCount
) {
}