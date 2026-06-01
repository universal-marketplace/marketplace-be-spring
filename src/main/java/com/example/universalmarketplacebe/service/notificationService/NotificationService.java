package com.example.universalmarketplacebe.service.notificationService;

import com.example.universalmarketplacebe.dto.response.NotificationDto;
import com.example.universalmarketplacebe.model.Notification;
import com.example.universalmarketplacebe.model.User;

import java.util.List;

public interface NotificationService {
    void createNotification(User user, String message, Long orderId);
    List<NotificationDto> getUserNotifications(String email);
    void markAsRead(Long notificationId, String email);
}
