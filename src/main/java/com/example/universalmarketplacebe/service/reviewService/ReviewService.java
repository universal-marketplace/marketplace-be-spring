package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;

public interface ReviewService {
    Review createReview();

    Reply replyToReview(Long id);
}
