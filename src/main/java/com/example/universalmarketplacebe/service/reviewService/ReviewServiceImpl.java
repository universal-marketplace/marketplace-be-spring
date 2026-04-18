package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.mapper.ReviewMapper;
import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.replyRepository.ReplyRepository;
import com.example.universalmarketplacebe.repository.reviewRepository.ReviewRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Transactional
    public ReviewDto createReview(ReviewCreateRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        
        User author = getCurrentUser();
        User targetUser = userRepository.findById(request.targetId()).orElseThrow(
                () -> new RuntimeException("Target user not found")
        );

        Review review = reviewMapper.toEntity(request);
        review.setAuthor(author);
        review.setTargetUser(targetUser);
        
        Review savedReview = reviewRepository.save(review);
        updateUserStats(targetUser);

        return reviewMapper.toDto(savedReview);
    }

    @Override
    @Transactional
    public ReviewDto replyToReview(Long reviewId, Long idReply, ReplyRequest replyRequest) {
        if (reviewId == null) {
            throw new IllegalArgumentException("Review ID cannot be null");
        }
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Reply commentForThis = (idReply != null)
                ? replyRepository.findById(idReply).orElse(null)
                : null;

        Reply reply = getReply(replyRequest, review, commentForThis);
        replyRepository.save(reply);

        return reviewMapper.toDto(review);
    }

    @Override
    @Transactional
    public ReviewDto updateReview(Long id, ReviewCreateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        checkOwnership(review.getAuthor());
        
        review.setRating(request.rating());
        review.setComment(request.comment());
        
        Review saved = reviewRepository.save(review);
        updateUserStats(review.getTargetUser());
        
        return reviewMapper.toDto(saved);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        checkOwnership(review.getAuthor());

        User targetUser = review.getTargetUser();
        // Usunięcie recenzji spowoduje kaskadowe usunięcie wszystkich odpowiedzi.
        reviewRepository.delete(review);
        updateUserStats(targetUser);
    }

    @Override
    @Transactional
    public ReviewDto updateReply(Long replyId, ReplyRequest request) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));
        
        checkOwnership(reply.getUser());
        
        reply.setComment(request.reply());
        replyRepository.save(reply);
        
        return reviewMapper.toDto(reply.getReview());
    }

    @Override
    @Transactional
    public void deleteReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found"));
        
        checkOwnership(reply.getUser());
        // Usunięcie odpowiedzi spowoduje kaskadowe usunięcie jej dzieci.
        replyRepository.delete(reply);
    }

    private void updateUserStats(User targetUser) {
        Double avgRating = reviewRepository.getAverageRatingForUser(targetUser.getId());
        Long count = reviewRepository.getReviewCountForUser(targetUser.getId());
        
        targetUser.setRating(avgRating != null ? avgRating : 0.0);
        targetUser.setReviewCount(count != null ? count.intValue() : 0);
        userRepository.save(targetUser);
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkOwnership(User author) {
        User currentUser = getCurrentUser();
        if (!author.getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to modify this content");
        }
    }

    private @NonNull Reply getReply(ReplyRequest replyRequest, Review review, Reply commentForThis) {
        User currentUser = getCurrentUser();
        Reply reply = new Reply();
        reply.setUser(currentUser);
        reply.setComment(replyRequest.reply());
        reply.setReview(review);
        reply.setParentReply(commentForThis);
        return reply;
    }
}
