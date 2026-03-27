package com.example.universalmarketplacebe.service.reviewService;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.mapper.ReviewMapper;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.repository.reviewRepository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    // Pomocnicza metoda do tworzenia ReviewDto
    private ReviewDto createMockReviewDto() {
        return new ReviewDto(
                1L, 1L, "Author Name", "avatar.png", 2L,
                5, "Great service!", LocalDateTime.now(),
                "Thanks!", LocalDateTime.now()
        );
    }

    // ==========================================
    // Tests for createReview(ReviewCreateRequest)
    // ==========================================

    @Test
    @DisplayName("createReview() - Happy Path: Powinien utworzyć i zwrócić nową recenzję")
    void createReview_HappyPath_ReturnsReviewDto() {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest(2L, 5, "Great service!");
        Review mockReview = new Review();
        ReviewDto expectedDto = createMockReviewDto();

        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toDto(mockReview)).thenReturn(expectedDto);

        // When
        ReviewDto result = reviewService.createReview(request);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.comment(), result.comment());
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("createReview() - Worse Path: Powinien rzucić wyjątek gdy docelowy użytkownik nie istnieje")
    void createReview_WorsePath_TargetUserNotFound() {
        // Given
        ReviewCreateRequest request = new ReviewCreateRequest(999L, 5, "Comment");
        
        // When & Then
        assertThrows(RuntimeException.class, () -> reviewService.createReview(request));
    }

    @Test
    @DisplayName("createReview() - Edge Case: Powinien rzucić IllegalArgumentException gdy request to null")
    void createReview_EdgeCase_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(null));
    }

    // ==========================================
    // Tests for replyToReview(Long, ReplyRequest)
    // ==========================================

    @Test
    @DisplayName("replyToReview() - Happy Path: Powinien dodać odpowiedź do istniejącej recenzji")
    void replyToReview_HappyPath_AddsReply() {
        // Given
        Long reviewId = 1L;
        ReplyRequest replyRequest = new ReplyRequest("Thank you for your feedback!");
        Review existingReview = new Review();
        existingReview.setReplies(new ArrayList<>());
        ReviewDto expectedDto = createMockReviewDto();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(any(Review.class))).thenReturn(existingReview);
        when(reviewMapper.toDto(existingReview)).thenReturn(expectedDto);

        // When
        ReviewDto result = reviewService.replyToReview(reviewId, replyRequest);

        // Then
        assertNotNull(result);
        verify(reviewRepository).findById(reviewId);
        verify(reviewRepository).save(existingReview);
        assertEquals(1, existingReview.getReplies().size());
        assertEquals(replyRequest.reply(), existingReview.getReplies().get(0).getComment());
    }

    @Test
    @DisplayName("replyToReview() - Worse Path: Powinien rzucić wyjątek gdy recenzja nie istnieje")
    void replyToReview_WorsePath_ReviewNotFound() {
        // Given
        Long reviewId = 999L;
        ReplyRequest replyRequest = new ReplyRequest("Reply");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> reviewService.replyToReview(reviewId, replyRequest));
    }

    @Test
    @DisplayName("replyToReview() - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null")
    void replyToReview_EdgeCase_NullId() {
        ReplyRequest replyRequest = new ReplyRequest("Reply");
        assertThrows(IllegalArgumentException.class, () -> reviewService.replyToReview(null, replyRequest));
    }
}
