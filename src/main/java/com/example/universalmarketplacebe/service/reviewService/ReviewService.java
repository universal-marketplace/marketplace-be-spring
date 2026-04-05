package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;

public interface ReviewService {
    ReviewDto createReview(ReviewCreateRequest request);

    ReviewDto replyToReview(Long reviewId, Long idReply, ReplyRequest replyRequest);
}
