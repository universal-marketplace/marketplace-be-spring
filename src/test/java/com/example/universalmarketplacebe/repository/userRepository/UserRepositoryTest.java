package com.example.universalmarketplacebe.repository.userRepository;

import com.example.universalmarketplacebe.model.User;
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
class UserRepositoryTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private UserRepository userRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldSaveAndFindUser() {
        // Given
        User user = new User();
        user.setName("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/avatar.jpg");
        
        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("testuser");
    }

    @Test
    void shouldFindUserByEmailWhenUserExists() {
        // Given
        String email = "findme@example.com";
        User user = new User();
        user.setName("findme");
        user.setEmail(email);
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/avatar.jpg");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail(email);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldReturnEmptyOptionalWhenEmailDoesNotExist() {
        // Given
        String nonExistentEmail = "notfound@example.com";

        // When
        Optional<User> foundUser = userRepository.findByEmail(nonExistentEmail);

        // Then
        assertThat(foundUser).isEmpty();
    }
}
