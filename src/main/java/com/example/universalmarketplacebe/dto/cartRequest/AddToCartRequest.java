package com.example.universalmarketplacebe.dto.cartRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotNull Long listingId,
        @NotNull @Min(1) Integer quantity
) {
}