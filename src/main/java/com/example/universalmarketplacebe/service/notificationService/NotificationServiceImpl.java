package com.example.universalmarketplacebe.service.notificationService;

import com.example.universalmarketplacebe.dto.response.NotificationDto;
import com.example.universalmarketplacebe.mapper.NotificationMapper;
import com.example.universalmarketplacebe.model.Notification;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.notificationRepository.NotificationRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional
    public void createNotification(User user, String message, Long orderId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setOrderId(orderId);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return notificationMapper.toDtoList(notificationRepository.findAllByUserOrderByCreatedAtDesc(user));
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, String email) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        
        if (!notification.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}
