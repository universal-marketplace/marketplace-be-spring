package com.example.universalmarketplacebe.repository.reviewRepository;

import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldSaveAndFindReview() {
        // Given
        User author = createAndSaveUser("author", "author@example.com");
        User target = createAndSaveUser("target", "target@example.com");

        Review review = new Review();
        review.setAuthor(author);
        review.setTargetUser(target);
        review.setRating(5);
        review.setComment("Great!");

        // When
        Review savedReview = reviewRepository.save(review);

        // Then
        assertThat(savedReview.getId()).isNotNull();

        Optional<Review> foundReview = reviewRepository.findById(savedReview.getId());
        assertThat(foundReview).isPresent();
        assertThat(foundReview.get().getComment()).isEqualTo("Great!");
    }

    @Test
    void shouldFindAllByTargetUserId() {
        // Given
        User author = createAndSaveUser("author2", "author2@example.com");
        User target1 = createAndSaveUser("target1", "target1@example.com");
        User target2 = createAndSaveUser("target2", "target2@example.com");

        createAndSaveReview(author, target1, 5, "Good");
        createAndSaveReview(author, target1, 4, "Nice");
        createAndSaveReview(author, target2, 3, "Okay");

        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Review> target1Reviews = reviewRepository.findAllByTargetUserId(target1.getId(), pageable);
        Page<Review> target2Reviews = reviewRepository.findAllByTargetUserId(target2.getId(), pageable);

        // Then
        assertThat(target1Reviews.getTotalElements()).isEqualTo(2);
        assertThat(target2Reviews.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldGetAverageRatingForUser() {
        // Given
        User author = createAndSaveUser("author3", "author3@example.com");
        User target = createAndSaveUser("target3", "target3@example.com");

        createAndSaveReview(author, target, 4, "Review 1");
        createAndSaveReview(author, target, 5, "Review 2");

        // When
        Double averageRating = reviewRepository.getAverageRatingForUser(target.getId());

        // Then
        assertThat(averageRating).isEqualTo(4.5);
    }

    @Test
    void shouldReturnNullAverageRatingWhenUserHasNoReviews() {
        // Given
        User target = createAndSaveUser("target4", "target4@example.com");

        // When
        Double averageRating = reviewRepository.getAverageRatingForUser(target.getId());

        // Then
        assertThat(averageRating).isNull();
    }

    @Test
    void shouldGetReviewCountForUser() {
        // Given
        User author = createAndSaveUser("author4", "author4@example.com");
        User target = createAndSaveUser("target5", "target5@example.com");

        createAndSaveReview(author, target, 5, "Review 1");
        createAndSaveReview(author, target, 4, "Review 2");
        createAndSaveReview(author, target, 3, "Review 3");

        // When
        Long count = reviewRepository.getReviewCountForUser(target.getId());

        // Then
        assertThat(count).isEqualTo(3L);
    }

    @Test
    void shouldReturnZeroReviewCountWhenUserHasNoReviews() {
        // Given
        User target = createAndSaveUser("target6", "target6@example.com");

        // When
        Long count = reviewRepository.getReviewCountForUser(target.getId());

        // Then
        assertThat(count).isEqualTo(0L);
    }

    private User createAndSaveUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/" + name + ".jpg");
        return userRepository.save(user);
    }

    private void createAndSaveReview(User author, User target, Integer rating, String comment) {
        Review review = new Review();
        review.setAuthor(author);
        review.setTargetUser(target);
        review.setRating(rating);
        review.setComment(comment);
        reviewRepository.save(review);
    }
}
