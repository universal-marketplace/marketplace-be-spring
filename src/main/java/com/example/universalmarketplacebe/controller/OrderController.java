package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.response.OrderResponse;
import com.example.universalmarketplacebe.model.OrderStatus;
import com.example.universalmarketplacebe.service.orderService.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/buyer")
    @Operation(summary = "Get buyer orders")
    public List<OrderResponse> getBuyerOrders(Authentication authentication) {
        return orderService.getBuyerOrders(authentication.getName());
    }

    @GetMapping("/seller")
    @Operation(summary = "Get seller orders")
    public List<OrderResponse> getSellerOrders(Authentication authentication) {
        return orderService.getSellerOrders(authentication.getName());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status (for sellers)")
    public OrderResponse updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String trackingNumber,
            Authentication authentication
    ) {
        return orderService.updateOrderStatus(id, status, trackingNumber, authentication.getName());
    }
}
