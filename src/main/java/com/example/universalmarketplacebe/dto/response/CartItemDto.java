package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Data Transfer Object representing an item in a cart")
public record CartItemDto(
        @Schema(description = "ID of the listing", example = "1")
        Long listingId,
        
        @Schema(description = "Title of the listing", example = "Vintage Camera")
        String title,
        
        @Schema(description = "Price per unit of the item", example = "150.00")
        BigDecimal unitPrice,
        
        @Schema(description = "Quantity of the item in the cart", example = "2")
        int quantity,
        
        @Schema(description = "Subtotal price for this item (unitPrice * quantity)", example = "300.00")
        BigDecimal subtotal
) {
}
