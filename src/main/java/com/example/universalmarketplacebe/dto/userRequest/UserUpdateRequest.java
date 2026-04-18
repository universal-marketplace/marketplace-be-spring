package com.example.universalmarketplacebe.dto.userRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Email String emailRepeated,
        String avatarUrl,
        String description
) {
}