package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing the authentication token")
public record AuthResponse(
        @Schema(description = "JWT authentication token", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token
) {
}
