package com.example.universalmarketplacebe.dto.cartRequest;

public record AddToCartRequest(
        Long listingId,
        Integer quantity
) {
}