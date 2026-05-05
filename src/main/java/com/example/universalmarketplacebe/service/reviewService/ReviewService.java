package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.request.ReplyRequest;
import com.example.universalmarketplacebe.dto.request.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.response.ReviewDto;

public interface ReviewService {
    ReviewDto createReview(ReviewCreateRequest request);

    ReviewDto replyToReview(Long reviewId, Long idReply, ReplyRequest replyRequest);

    ReviewDto updateReview(Long id, ReviewCreateRequest request);

    void deleteReview(Long id);

    ReviewDto updateReply(Long replyId, ReplyRequest request);

    void deleteReply(Long replyId);
}
