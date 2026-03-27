package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.service.reviewService.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ReviewDto createReview(ReviewCreateRequest request) {
        return reviewService.createReview(request);
    }

    @PostMapping("/{id}/reply")
    public ReviewDto replyToReview(@PathVariable Long id, @RequestBody ReplyRequest replyRequest) {
        return reviewService.replyToReview(id, replyRequest);
    }
}
