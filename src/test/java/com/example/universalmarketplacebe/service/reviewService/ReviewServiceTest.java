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
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReplyRepository replyRepository;

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
        Long targetId = 2L;
        ReviewCreateRequest request = new ReviewCreateRequest(targetId, 5, "Great service!");
        Review mockReview = new Review();
        ReviewDto expectedDto = createMockReviewDto();

        when(userRepository.findById(targetId)).thenReturn(Optional.of(new User()));
        when(reviewMapper.toEntity(request)).thenReturn(mockReview);
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toDto(mockReview)).thenReturn(expectedDto);

        // When
        ReviewDto result = reviewService.createReview(request);

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.comment(), result.comment());
        verify(userRepository).findById(targetId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("createReview() - Worse Path: Powinien rzucić wyjątek gdy docelowy użytkownik nie istnieje")
    void createReview_WorsePath_TargetUserNotFound() {
        // Given
        Long targetId = 999L;
        ReviewCreateRequest request = new ReviewCreateRequest(targetId, 5, "Comment");
        when(userRepository.findById(targetId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> reviewService.createReview(request));
    }

    @Test
    @DisplayName("createReview() - Edge Case: Powinien rzucić IllegalArgumentException gdy request to null")
    void createReview_EdgeCase_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(null));
    }

    // ==========================================
    // Tests for replyToReview(Long, Long, ReplyRequest)
    // ==========================================

    @Test
    @DisplayName("replyToReview() - Happy Path: Powinien dodać odpowiedź do istniejącej recenzji")
    void replyToReview_HappyPath_AddsReply() {
        // Given
        Long reviewId = 1L;
        Long idReply = 2L;
        ReplyRequest replyRequest = new ReplyRequest("Thank you for your feedback!");
        Review existingReview = new Review();
        existingReview.setReplies(new ArrayList<>());
        
        Reply existingReply = new Reply();
        existingReply.setChildReplies(new ArrayList<>());

        ReviewDto expectedDto = createMockReviewDto();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(replyRepository.findById(idReply)).thenReturn(Optional.of(existingReply));
        when(replyRepository.save(any(Reply.class))).thenReturn(new Reply());
        when(reviewMapper.toDto(existingReview)).thenReturn(expectedDto);

        // When
        ReviewDto result = reviewService.replyToReview(reviewId, idReply, replyRequest);

        // Then
        assertNotNull(result);
        verify(reviewRepository).findById(reviewId);
        verify(replyRepository).findById(idReply);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("replyToReview() - Worse Path: Powinien rzucić wyjątek gdy recenzja nie istnieje")
    void replyToReview_WorsePath_ReviewNotFound() {
        // Given
        Long reviewId = 999L;
        Long idReply = 999L;
        ReplyRequest replyRequest = new ReplyRequest("Reply");
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> reviewService.replyToReview(reviewId, idReply, replyRequest));
    }

    @Test
    @DisplayName("replyToReview() - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null")
    void replyToReview_EdgeCase_NullId() {
        ReplyRequest replyRequest = new ReplyRequest("Reply");
        assertThrows(IllegalArgumentException.class, () -> reviewService.replyToReview(null, null, replyRequest));
    }
}
