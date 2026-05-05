package com.example.universalmarketplacebe.repository.notificationRepository;

import com.example.universalmarketplacebe.model.Notification;
import com.example.universalmarketplacebe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByUserOrderByCreatedAtDesc(User user);
    long countByUserAndIsReadFalse(User user);
}
