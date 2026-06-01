package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.response.NotificationDto;
import com.example.universalmarketplacebe.service.notificationService.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Endpoints for managing user notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Get user notifications", description = "Returns a list of notifications for the current user")
    public List<NotificationDto> getNotifications(Authentication authentication) {
        return notificationService.getUserNotifications(authentication.getName());
    }

    @PatchMapping("/{id}/read")
    @Operation(summary = "Mark notification as read")
    public void markAsRead(@PathVariable Long id, Authentication authentication) {
        notificationService.markAsRead(id, authentication.getName());
    }
}
