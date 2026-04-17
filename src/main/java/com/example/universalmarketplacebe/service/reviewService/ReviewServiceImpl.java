package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.mapper.ReviewMapper;
import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.repository.replyRepository.ReplyRepository;
import com.example.universalmarketplacebe.repository.reviewRepository.ReviewRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReplyRepository replyRepository;
    private final ReviewMapper reviewMapper;

    @Override
    public ReviewDto createReview(ReviewCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        Long targetId = request.targetId();
        userRepository.findById(targetId).orElseThrow(
                () -> new RuntimeException("Target user not found")
        );

        Review review = reviewMapper.toEntity(request);

        return reviewMapper.toDto(
                reviewRepository.save(review)
        );
    }

    @Override
    @Transactional
    public ReviewDto replyToReview(Long reviewId, Long idReply, ReplyRequest replyRequest) {
        if (reviewId == null) {
            throw new IllegalArgumentException("Review ID cannot be null");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(
                        () -> new RuntimeException("Review not found"));

        Reply commentForThis = (idReply != null)
                ? replyRepository.findById(idReply).orElse(null)
                : null;

        Reply reply = getReply(replyRequest, review, commentForThis);

        replyRepository.save(reply);

        return reviewMapper.toDto(review);
    }

    private static @NonNull Reply getReply(ReplyRequest replyRequest, Review review, Reply commentForThis) {
        Reply reply = new Reply();
        reply.setUser(null);
        reply.setComment(replyRequest.reply());
        reply.setReview(review);
        reply.setParentReply(commentForThis);
        return reply;
    }
}
