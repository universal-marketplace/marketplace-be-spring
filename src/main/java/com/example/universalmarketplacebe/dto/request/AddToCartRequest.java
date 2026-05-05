package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Request to add an item to the shopping cart")
public record AddToCartRequest(
        @Schema(description = "ID of the listing to be added", example = "1")
        @NotNull Long listingId,
        
        @Schema(description = "Quantity of the item to add", example = "2")
        @Min(1) int quantity,
        
        @Schema(description = "Optional booking date for services", example = "2024-05-20")
        LocalDate bookingDate
) {
}
