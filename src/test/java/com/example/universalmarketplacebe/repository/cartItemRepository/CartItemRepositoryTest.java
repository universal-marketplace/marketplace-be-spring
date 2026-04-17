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
        User user = new User();
        user.setName("buyer");
        user.setEmail("buyer@example.com");
        user.setPassword("password");
        user.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(user);

        User seller = new User();
        seller.setName("seller");
        seller.setEmail("seller@example.com");
        seller.setPassword("password");
        seller.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(seller);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        Listing listing = new Listing();
        listing.setTitle("Test Listing");
        listing.setDescription("Test Description");
        listing.setPrice(BigDecimal.valueOf(100));
        listing.setImageUrl("http://example.com/image.png");
        listing.setAdvertiser(seller);
        listing.setType(Type.ITEM);
        listingRepository.save(listing);

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
        assertThat(foundItem.get().getListing().getId()).isEqualTo(listing.getId());
        assertThat(foundItem.get().getCart().getId()).isEqualTo(cart.getId());
    }

    @Test
    void shouldReturnEmptyWhenCartItemDoesNotExistForCartAndListing() {
        // Given
        User user = new User();
        user.setName("buyer2");
        user.setEmail("buyer2@example.com");
        user.setPassword("password");
        user.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(user);

        User seller = new User();
        seller.setName("seller2");
        seller.setEmail("seller2@example.com");
        seller.setPassword("password");
        seller.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(seller);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        Listing listing = new Listing();
        listing.setTitle("Test Listing 2");
        listing.setDescription("Test Description 2");
        listing.setPrice(BigDecimal.valueOf(200));
        listing.setImageUrl("http://example.com/image.png");
        listing.setAdvertiser(seller);
        listing.setType(Type.ITEM);
        listingRepository.save(listing);

        // When
        Optional<CartItem> notFoundItem = cartItemRepository.findCartItemByCartAndListing(cart, listing);

        // Then
        assertThat(notFoundItem).isEmpty();
    }
}