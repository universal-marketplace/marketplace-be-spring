package com.example.universalmarketplacebe.dto.request;

import com.example.universalmarketplacebe.model.DeliveryMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to finalize the purchase and checkout")
public record CheckoutRequest(
    @Schema(description = "Selected delivery method (required if cart contains items, ignored for services)", example = "SHIPPING")
    DeliveryMethod deliveryMethod
) {
}
