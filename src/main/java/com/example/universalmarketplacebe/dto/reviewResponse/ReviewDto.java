package com.example.universalmarketplacebe.dto.reviewResponse;

import java.time.LocalDateTime;

public record ReviewDto(
        Long id,
        Long authorId,
        String authorName,
        String authorAvatar,
        Long targetId,
        Integer rating,
        String comment,
        LocalDateTime date,
        String reply,
        LocalDateTime replyDate
) {
}