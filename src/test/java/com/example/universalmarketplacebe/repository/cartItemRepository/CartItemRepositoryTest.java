package com.example.universalmarketplacebe.repository.cartItemRepository;

import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.Type;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
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
class CartItemRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldFindCartItemByCartAndListing() {
        // Given
        User buyer = createAndSaveUser("buyer", "buyer@example.com");
        User seller = createAndSaveUser("seller", "seller@example.com");

        Cart cart = createAndSaveCart(buyer);
        Listing listing = createAndSaveListing(seller, "Test Listing", 100.0);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setListing(listing);
        cartItem.setQuantity(2);
        cartItemRepository.save(cartItem);

        // When
        Optional<CartItem> foundItem = cartItemRepository.findCartItemByCartAndListing(cart, listing);

        // Then
        assertThat(foundItem).isPresent();
        assertThat(foundItem.get().getQuantity()).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptyWhenCartItemDoesNotExistForCartAndListing() {
        // Given
        User buyer = createAndSaveUser("buyer2", "buyer2@example.com");
        User seller = createAndSaveUser("seller2", "seller2@example.com");

        Cart cart = createAndSaveCart(buyer);
        Listing listing = createAndSaveListing(seller, "Test Listing 2", 200.0);

        // When
        Optional<CartItem> notFoundItem = cartItemRepository.findCartItemByCartAndListing(cart, listing);

        // Then
        assertThat(notFoundItem).isEmpty();
    }

    private User createAndSaveUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/avatar.png");
        return userRepository.save(user);
    }

    private Cart createAndSaveCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    private Listing createAndSaveListing(User advertiser, String title, Double price) {
        Listing listing = new Listing();
        listing.setTitle(title);
        listing.setDescription("Description");
        listing.setPrice(BigDecimal.valueOf(price));
        listing.setImageUrl("https://example.com/image.png");
        listing.setAdvertiser(advertiser);
        listing.setType(Type.ITEM);
        return listingRepository.save(listing);
    }
}
