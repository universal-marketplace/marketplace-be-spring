package com.example.universalmarketplacebe.dto.listingRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record ListingRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull BigDecimal priceAmount,
        @NotBlank String currency,
        Integer unitAmount,
        @NotBlank String imageUrl,
        @NotNull List<String> tags,
        @NotBlank String type      // "ITEM" lub "SERVICE"
) {
}