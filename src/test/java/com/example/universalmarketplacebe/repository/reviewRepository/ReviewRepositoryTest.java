package com.example.universalmarketplacebe.repository.reviewRepository;

import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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
        User author = new User();
        author.setName("author");
        author.setEmail("author@example.com");
        author.setPassword("password");
        author.setAvatarUrl("http://example.com/author.jpg");
        userRepository.save(author);

        User target = new User();
        target.setName("target");
        target.setEmail("target@example.com");
        target.setPassword("password");
        target.setAvatarUrl("http://example.com/target.jpg");
        userRepository.save(target);

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
}
