package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Data Transfer Object for user notifications")
public record NotificationDto(
    @Schema(description = "Notification ID", example = "1")
    Long id,
    @Schema(description = "Message content", example = "Nowe zamówienie #123")
    String message,
    @Schema(description = "Associated order ID", example = "123")
    Long orderId,
    @Schema(description = "Is the notification read?", example = "false")
    boolean isRead,
    @Schema(description = "Creation timestamp")
    LocalDateTime createdAt
) {
}
