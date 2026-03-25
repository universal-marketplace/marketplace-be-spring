package com.example.universalmarketplacebe.dto.userRequest;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String name,
        String email,
        String emailRepeated,
        String avatarUrl,
        String description
) {
}