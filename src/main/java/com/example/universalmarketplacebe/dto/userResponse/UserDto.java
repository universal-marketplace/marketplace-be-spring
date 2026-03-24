package com.example.universalmarketplacebe.dto.userResponse;

public record UserDto(
        Long id,
        String name,
        String email,
        String avatarUrl,
        String description,
        Double rating,
        Integer reviewCount
) {
}