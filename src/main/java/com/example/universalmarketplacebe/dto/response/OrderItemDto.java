package com.example.universalmarketplacebe.dto.response;

import com.example.universalmarketplacebe.model.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Data Transfer Object for an item within an order")
public record OrderItemDto(
    @Schema(description = "Listing ID", example = "1")
    Long listingId,
    @Schema(description = "Listing title", example = "Rower Górski")
    String title,
    @Schema(description = "Listing type", example = "ITEM")
    Type type,
    @Schema(description = "Quantity", example = "1")
    int quantity,
    @Schema(description = "Unit price at purchase time", example = "100.00")
    BigDecimal unitPrice,
    @Schema(description = "Optional booking date", example = "2024-05-20")
    LocalDate bookingDate
) {
}
