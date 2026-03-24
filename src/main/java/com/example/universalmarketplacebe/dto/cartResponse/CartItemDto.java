package com.example.universalmarketplacebe.dto.cartResponse;

import java.math.BigDecimal;

public record CartItemDto(
        Long listingId,
        String title,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal subtotal
) {
}