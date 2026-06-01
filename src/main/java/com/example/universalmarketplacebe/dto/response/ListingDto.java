package com.example.universalmarketplacebe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Data Transfer Object representing a listing")
public record ListingDto(
        @Schema(description = "Unique identifier of the listing", example = "1")
        Long id,
        
        @Schema(description = "Title of the listing", example = "Vintage Camera")
        String title,
        
        @Schema(description = "Detailed description of the listing", example = "A well-preserved 1970s film camera.")
        String description,
        
        @Schema(description = "Price of the listing", example = "150.00")
        BigDecimal price,
        
        @Schema(description = "Available quantity (for items)", example = "1")
        Integer unitAmount,
        
        @Schema(description = "Price unit (for services, e.g., 'PER_HOUR', 'PER_M2', 'PER_SERVICE')", example = "PER_HOUR")
        String priceUnit,
        
        @Schema(description = "URL of the listing image", example = "https://example.com/images/camera.jpg")
        String imageUrl,
        
        @Schema(description = "ID of the user who posted the listing", example = "5")
        Long advertiserId,
        
        @Schema(description = "Name of the user who posted the listing", example = "John Doe")
        String advertiserName,
        
        @Schema(description = "Avatar URL of the user who posted the listing", example = "https://example.com/avatars/jdoe.png")
        String advertiserAvatar,
        
        @Schema(description = "Average rating of the advertiser", example = "4.8")
        Double rating,
        
        @Schema(description = "Total number of reviews for the advertiser", example = "25")
        Integer reviewCount,
        
        @Schema(description = "List of tags associated with the listing", example = "[\"electronics\", \"vintage\"]")
        List<String> tags,
        
        @Schema(description = "Type of listing: ITEM or SERVICE", example = "ITEM")
        String type
) {
}
