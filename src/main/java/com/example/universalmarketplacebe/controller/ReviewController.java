package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.service.reviewService.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review createReview() {
        return reviewService.createReview();
    }

    @PostMapping("/{id}/reply")
    public Reply replyToReview(@PathVariable Long id) {
        return reviewService.replyToReview(id);
    }
}
