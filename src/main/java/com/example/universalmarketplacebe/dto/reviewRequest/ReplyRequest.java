package com.example.universalmarketplacebe.dto.reviewRequest;

import jakarta.validation.constraints.NotBlank;

public record ReplyRequest(
        @NotBlank String reply
) {
}