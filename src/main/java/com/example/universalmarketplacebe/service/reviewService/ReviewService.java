package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;

public interface ReviewService {
    ReviewDto createReview();

    ReviewDto replyToReview(Long id);
}
