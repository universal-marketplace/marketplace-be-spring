package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Data Transfer Object representing a reply to a review")
public record ReplyDto(
        @Schema(description = "Unique identifier of the reply", example = "1")
        Long id,
        @Schema(description = "ID of the author of the reply", example = "1")
        Long authorId,
        @Schema(description = "Name of the author of the reply", example = "Jane Doe")
        String authorName,
        @Schema(description = "Avatar URL of the author of the reply", example = "https://example.com/avatar.jpg")
        String authorAvatar,
        @Schema(description = "Comment content of the reply", example = "Thank you for your feedback!")
        String comment,
        @Schema(description = "Timestamp when the reply was created")
        LocalDateTime createdAt,
        @Schema(description = "List of nested replies")
        List<ReplyDto> replies
) {
}
