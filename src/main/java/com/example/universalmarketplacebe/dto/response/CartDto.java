package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Data Transfer Object representing a shopping cart")
public record CartDto(
        @Schema(description = "List of items in the cart")
        List<CartItemDto> items,
        
        @Schema(description = "Total price of all items in the cart", example = "299.99")
        BigDecimal totalPrice
) {
}
