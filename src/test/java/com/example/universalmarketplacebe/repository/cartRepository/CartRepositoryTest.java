package com.example.universalmarketplacebe.repository.cartRepository;

import com.example.universalmarketplacebe.model.Cart;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CartRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldSaveAndFindCart() {
        // Given
        User user = createAndSaveUser("testuser", "test@example.com");

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);

        // When
        Cart savedCart = cartRepository.save(cart);

        // Then
        assertThat(savedCart.getId()).isNotNull();

        Optional<Cart> foundCart = cartRepository.findById(savedCart.getId());
        assertThat(foundCart).isPresent();
    }

    @Test
    void shouldFindCartByUserEmail() {
        // Given
        String email = "john.doe@example.com";
        User user = createAndSaveUser("John Doe", email);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        // When
        Optional<Cart> foundCart = cartRepository.findByUserEmail(email);

        // Then
        assertThat(foundCart).isPresent();
        assertThat(foundCart.get().getUser().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldReturnEmptyWhenCartDoesNotExistForUserEmail() {
        // Given
        String email = "non.existent@example.com";

        // When
        Optional<Cart> notFoundCart = cartRepository.findByUserEmail(email);

        // Then
        assertThat(notFoundCart).isEmpty();
    }

    private User createAndSaveUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/avatar.png");
        return userRepository.save(user);
    }
}
