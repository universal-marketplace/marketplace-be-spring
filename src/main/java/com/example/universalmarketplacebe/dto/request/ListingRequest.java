package com.example.universalmarketplacebe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request to create or update a listing")
public record ListingRequest(
        @Schema(description = "Title of the listing", example = "Vintage Camera")
        @NotBlank String title,
        
        @Schema(description = "Detailed description of the listing", example = "A well-preserved 1970s film camera.")
        @NotBlank String description,
        
        @Schema(description = "Price of the item or service", example = "150.00")
        @NotNull BigDecimal priceAmount,
        
        @Schema(description = "Available quantity (for items)", example = "1")
        @Min(0) Integer unitAmount,
        
        @Schema(description = "URL of the listing image", example = "https://example.com/images/camera.jpg")
        @NotBlank String imageUrl,
        
        @Schema(description = "List of tags for categorization", example = "[\"electronics\", \"vintage\", \"photography\"]")
        @NotNull List<String> tags,
        
        @Schema(description = "Type of the listing: ITEM or SERVICE", example = "ITEM")
        @NotBlank String type
) {
}
