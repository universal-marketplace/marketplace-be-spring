package com.example.universalmarketplacebe.service.orderService;

import com.example.universalmarketplacebe.dto.response.OrderResponse;
import com.example.universalmarketplacebe.model.OrderStatus;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getBuyerOrders(String email);
    List<OrderResponse> getSellerOrders(String email);
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status, String trackingNumber, String email);
}
