package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to reply to a review")
public record ReplyRequest(
        @Schema(description = "Content of the reply", example = "Thank you for your feedback!")
        @NotBlank String reply
) {
}
