package com.example.universalmarketplacebe.dto.reviewResponse;

import java.time.LocalDateTime;
import java.util.List;

public record ReplyDto(
        Long id,
        Long authorId,
        String authorName,
        String authorAvatar,
        String comment,
        LocalDateTime createdAt,
        List<ReplyDto> replies
) {
}
