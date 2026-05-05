package com.example.universalmarketplacebe.service.orderService;

import com.example.universalmarketplacebe.dto.response.OrderResponse;
import com.example.universalmarketplacebe.mapper.OrderMapper;
import com.example.universalmarketplacebe.model.DeliveryMethod;
import com.example.universalmarketplacebe.model.Order;
import com.example.universalmarketplacebe.model.OrderStatus;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.orderRepository.OrderRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import com.example.universalmarketplacebe.service.notificationService.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getBuyerOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderMapper.toDtoList(orderRepository.findAllByBuyerOrderByCreatedAtDesc(user));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getSellerOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderMapper.toDtoList(orderRepository.findAllBySeller(user));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, String trackingNumber, String email) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        // Verify user is the seller for at least one item in this order
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        boolean isSeller = order.getItems().stream()
                .anyMatch(i -> i.getListing().getAdvertiser().getId().equals(user.getId()));
        
        if (!isSeller) {
            throw new RuntimeException("Unauthorized to update order status");
        }

        // Handle tracking number
        if (trackingNumber != null && !trackingNumber.isBlank()) {
            order.setTrackingNumber(trackingNumber);
        }

        // Validation: Cannot complete shipping order without tracking number
        if (OrderStatus.COMPLETED.equals(status) 
                && DeliveryMethod.SHIPPING.equals(order.getDeliveryMethod())
                && (order.getTrackingNumber() == null || order.getTrackingNumber().isBlank())) {
            throw new IllegalArgumentException("Cannot complete shipping order without a tracking number");
        }
        
        order.setStatus(status);
        orderRepository.save(order);
        
        // Notify buyer
        String message = String.format("Status Twojego zamówienia #%d zmienił się na: %s", order.getId(), status.name());
        if (order.getTrackingNumber() != null) {
            message += " Numer paczki: " + order.getTrackingNumber();
        }
        notificationService.createNotification(order.getBuyer(), message, order.getId());
        
        return orderMapper.toDto(order);
    }
}
