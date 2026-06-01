package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Data Transfer Object representing a review")
public record ReviewDto(
        @Schema(description = "Unique identifier of the review", example = "1")
        Long id,
        @Schema(description = "ID of the author of the review", example = "1")
        Long authorId,
        @Schema(description = "Name of the author of the review", example = "John Doe")
        String authorName,
        @Schema(description = "Avatar URL of the author of the review", example = "https://example.com/avatar.jpg")
        String authorAvatar,
        @Schema(description = "ID of the target (listing or user) being reviewed", example = "2")
        Long targetId,
        @Schema(description = "Rating given in the review", example = "5")
        Integer rating,
        @Schema(description = "Comment content of the review", example = "Great product!")
        String comment,
        @Schema(description = "Timestamp when the review was created")
        LocalDateTime date,
        @Schema(description = "List of replies to this review")
        List<ReplyDto> replies
) {
}
