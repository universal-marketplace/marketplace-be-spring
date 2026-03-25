package com.example.universalmarketplacebe.dto.cartResponse;

import java.math.BigDecimal;
import java.util.List;

public record CartDto(
        List<CartItemDto> items,
        BigDecimal totalPrice
) {
}