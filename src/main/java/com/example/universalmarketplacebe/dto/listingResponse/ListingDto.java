package com.example.universalmarketplacebe.dto.listingResponse;

import java.math.BigDecimal;
import java.util.List;

public record ListingDto(
        Long id,
        String title,
        String description,
        BigDecimal price,
        String imageUrl,
        Long advertiserId,
        String advertiserName,
        String advertiserAvatar,
        Double rating,
        Integer reviewCount,
        List<String> tags,
        String type
) {
}