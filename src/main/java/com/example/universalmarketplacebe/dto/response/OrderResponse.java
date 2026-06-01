package com.example.universalmarketplacebe.dto.response;

import com.example.universalmarketplacebe.model.DeliveryMethod;
import com.example.universalmarketplacebe.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Data Transfer Object for order details")
public record OrderResponse(
    @Schema(description = "Order ID", example = "123")
    Long id,
    @Schema(description = "Total price", example = "250.00")
    BigDecimal totalPrice,
    @Schema(description = "Delivery method", example = "SHIPPING")
    DeliveryMethod deliveryMethod,
    @Schema(description = "Current status", example = "PENDING")
    OrderStatus status,
    @Schema(description = "Creation date")
    LocalDateTime createdAt,
    @Schema(description = "Tracking number for shipping", example = "XYZ123456")
    String trackingNumber,
    @Schema(description = "Items in the order")
    List<OrderItemDto> items
) {
}
