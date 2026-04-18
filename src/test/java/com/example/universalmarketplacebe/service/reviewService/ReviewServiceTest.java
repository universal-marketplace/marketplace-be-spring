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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    private User createMockUser(Long id) {
        User user = new User();
        user.setId(id);
        user.setEmail("user" + id + "@example.com");
        return user;
    }

    private ReviewDto createMockReviewDto() {
        return new ReviewDto(
                1L, 1L, "Author Name", "avatar.png", 2L,
                5, "Great service!", LocalDateTime.now(),
                List.of()
        );
    }

    // ==========================================
    // Tests for createReview()
    // ==========================================

    @Test
    @DisplayName("createReview() - Happy Path")
    void createReview_HappyPath() {
        Long targetId = 2L;
        ReviewCreateRequest request = new ReviewCreateRequest(targetId, 5, "Great service!");
        Review mockReview = new Review();
        mockReview.setTargetUser(new User());
        ReviewDto expectedDto = createMockReviewDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(createMockUser(1L));
        when(userRepository.findById(targetId)).thenReturn(Optional.of(new User()));
        when(reviewMapper.toEntity(request)).thenReturn(mockReview);
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);
        when(reviewMapper.toDto(mockReview)).thenReturn(expectedDto);

        ReviewDto result = reviewService.createReview(request);

        assertNotNull(result);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    @DisplayName("createReview() - Worse Path: Target User Not Found")
    void createReview_TargetUserNotFound() {
        Long targetId = 999L;
        ReviewCreateRequest request = new ReviewCreateRequest(targetId, 5, "Comment");
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(createMockUser(1L));
        when(userRepository.findById(targetId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reviewService.createReview(request));
    }

    @Test
    @DisplayName("createReview() - Edge Case: Null Request")
    void createReview_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(null));
    }

    // ==========================================
    // Tests for replyToReview()
    // ==========================================

    @Test
    @DisplayName("replyToReview() - Happy Path")
    void replyToReview_HappyPath() {
        Long reviewId = 1L;
        ReplyRequest replyRequest = new ReplyRequest("Thank you!");
        Review existingReview = new Review();
        existingReview.setReplies(new ArrayList<>());

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(createMockUser(1L));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewMapper.toDto(existingReview)).thenReturn(createMockReviewDto());

        ReviewDto result = reviewService.replyToReview(reviewId, null, replyRequest);

        assertNotNull(result);
        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("replyToReview() - Happy Path: Reply to another reply")
    void replyToReview_ReplyToReply() {
        Long reviewId = 1L;
        Long replyId = 2L;
        ReplyRequest replyRequest = new ReplyRequest("Thank you!");
        Review existingReview = new Review();
        Reply existingReply = new Reply();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(createMockUser(1L));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(replyRepository.findById(replyId)).thenReturn(Optional.of(existingReply));
        when(reviewMapper.toDto(existingReview)).thenReturn(createMockReviewDto());

        reviewService.replyToReview(reviewId, replyId, replyRequest);

        verify(replyRepository).save(any(Reply.class));
    }

    @Test
    @DisplayName("replyToReview() - Edge Case: Null ReviewId")
    void replyToReview_NullReviewId() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.replyToReview(null, null, new ReplyRequest("Test")));
    }

    @Test
    @DisplayName("replyToReview() - Worse Path: Review Not Found")
    void replyToReview_ReviewNotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reviewService.replyToReview(1L, null, new ReplyRequest("Test")));
    }

    // ==========================================
    // Tests for update/delete Review
    // ==========================================

    @Test
    @DisplayName("updateReview() - Happy Path")
    void updateReview_HappyPath() {
        Long id = 1L;
        ReviewCreateRequest request = new ReviewCreateRequest(2L, 4, "Updated");
        User author = createMockUser(1L);
        Review review = new Review();
        review.setAuthor(author);
        review.setTargetUser(new User());

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(author);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMapper.toDto(review)).thenReturn(createMockReviewDto());

        reviewService.updateReview(id, request);

        assertEquals(4, review.getRating());
        verify(reviewRepository).save(review);
    }

    @Test
    @DisplayName("updateReview() - Worse Path: Not Found")
    void updateReview_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reviewService.updateReview(1L, new ReviewCreateRequest(2L, 5, "Test")));
    }

    @Test
    @DisplayName("updateReview() - Worse Path: Unauthorized")
    void updateReview_Unauthorized() {
        Long id = 1L;
        User author = createMockUser(1L);
        User otherUser = createMockUser(2L);
        Review review = new Review();
        review.setAuthor(author);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(otherUser);

        assertThrows(RuntimeException.class, () -> reviewService.updateReview(id, new ReviewCreateRequest(2L, 5, "Test")));
    }

    @Test
    @DisplayName("deleteReview() - Happy Path")
    void deleteReview_HappyPath() {
        Long id = 1L;
        User author = createMockUser(1L);
        Review review = new Review();
        review.setAuthor(author);
        review.setTargetUser(new User());

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(author);

        reviewService.deleteReview(id);

        verify(reviewRepository).delete(review);
    }

    @Test
    @DisplayName("deleteReview() - Worse Path: Not Found")
    void deleteReview_NotFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reviewService.deleteReview(1L));
    }

    @Test
    @DisplayName("deleteReview() - Worse Path: Unauthorized")
    void deleteReview_Unauthorized() {
        Long id = 1L;
        User author = createMockUser(1L);
        User otherUser = createMockUser(2L);
        Review review = new Review();
        review.setAuthor(author);

        when(reviewRepository.findById(id)).thenReturn(Optional.of(review));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(otherUser);

        assertThrows(RuntimeException.class, () -> reviewService.deleteReview(id));
    }

    // ==========================================
    // Tests for update/delete Reply
    // ==========================================

    @Test
    @DisplayName("updateReply() - Happy Path")
    void updateReply_HappyPath() {
        Long replyId = 1L;
        ReplyRequest request = new ReplyRequest("Updated Reply");
        User user = createMockUser(1L);
        Reply reply = new Reply();
        reply.setUser(user);
        reply.setReview(new Review());

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(reviewMapper.toDto(any())).thenReturn(createMockReviewDto());

        reviewService.updateReply(replyId, request);

        assertEquals("Updated Reply", reply.getComment());
        verify(replyRepository).save(reply);
    }

    @Test
    @DisplayName("updateReply() - Worse Path: Not Found")
    void updateReply_NotFound() {
        when(replyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reviewService.updateReply(1L, new ReplyRequest("Test")));
    }

    @Test
    @DisplayName("updateReply() - Worse Path: Unauthorized")
    void updateReply_Unauthorized() {
        Long replyId = 1L;
        User user = createMockUser(1L);
        User otherUser = createMockUser(2L);
        Reply reply = new Reply();
        reply.setUser(user);

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(otherUser);

        assertThrows(RuntimeException.class, () -> reviewService.updateReply(replyId, new ReplyRequest("Test")));
    }

    @Test
    @DisplayName("deleteReply() - Happy Path")
    void deleteReply_HappyPath() {
        Long replyId = 1L;
        User user = createMockUser(1L);
        Reply reply = new Reply();
        reply.setUser(user);

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        reviewService.deleteReply(replyId);

        verify(replyRepository).delete(reply);
    }

    @Test
    @DisplayName("deleteReply() - Worse Path: Not Found")
    void deleteReply_NotFound() {
        when(replyRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> reviewService.deleteReply(1L));
    }

    @Test
    @DisplayName("deleteReply() - Worse Path: Unauthorized")
    void deleteReply_Unauthorized() {
        Long replyId = 1L;
        User user = createMockUser(1L);
        User otherUser = createMockUser(2L);
        Reply reply = new Reply();
        reply.setUser(user);

        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(otherUser);

        assertThrows(RuntimeException.class, () -> reviewService.deleteReply(replyId));
    }
}
