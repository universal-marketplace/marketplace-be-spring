package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.dto.cartResponse.CartItemDto;
import com.example.universalmarketplacebe.mapper.CartMapper;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.cartItemRepository.CartItemRepository;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ListingRepository listingRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    private User createMockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        return user;
    }

    private Listing createMockListing(Long id) {
        Listing listing = new Listing();
        listing.setId(id);
        listing.setPrice(new BigDecimal("100.00"));
        return listing;
    }

    private CartDto createMockCartDto() {
        CartItemDto item = new CartItemDto(1L, "Listing Title", new BigDecimal("100.00"), 1, new BigDecimal("100.00"));
        return new CartDto(List.of(item), new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("getCart() - Happy Path")
    void getCart_HappyPath() {
        String email = "test@example.com";
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        CartDto dto = createMockCartDto();

        when(cartRepository.findByUserEmail(email)).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(cart)).thenReturn(dto);

        CartDto result = cartService.getCart(email);

        assertNotNull(result);
        verify(cartRepository).findByUserEmail(email);
    }

    @Test
    @DisplayName("addItemToCart() - Happy Path")
    void addItemToCart_HappyPath() {
        String email = "test@example.com";
        AddToCartRequest request = new AddToCartRequest(1L, 1);
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        Listing listing = createMockListing(1L);
        CartDto dto = createMockCartDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(cartRepository.findByUserEmail(email)).thenReturn(Optional.of(cart));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(cartItemRepository.findCartItemByCartAndListing(cart, listing)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(dto);

        CartDto result = cartService.addItemToCart(request);

        assertNotNull(result);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("updateItemInCart() - Happy Path")
    void updateItemInCart_HappyPath() {
        String email = "test@example.com";
        AddToCartRequest request = new AddToCartRequest(1L, 5);
        Cart cart = new Cart();
        Listing listing = createMockListing(1L);
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        CartDto dto = createMockCartDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(cartRepository.findByUserEmail(email)).thenReturn(Optional.of(cart));
        when(listingRepository.findById(1L)).thenReturn(Optional.of(listing));
        when(cartItemRepository.findCartItemByCartAndListing(cart, listing)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(dto);

        CartDto result = cartService.updateItemInCart(request);

        assertNotNull(result);
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    @DisplayName("removeItemFromCart() - Happy Path")
    void removeItemFromCart_HappyPath() {
        String email = "test@example.com";
        Long listingId = 1L;
        Cart cart = new Cart();
        cart.setCartItems(new ArrayList<>());
        Listing listing = createMockListing(listingId);
        CartItem cartItem = new CartItem();
        cart.getCartItems().add(cartItem);
        CartDto dto = createMockCartDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(cartRepository.findByUserEmail(email)).thenReturn(Optional.of(cart));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(cartItemRepository.findCartItemByCartAndListing(cart, listing)).thenReturn(Optional.of(cartItem));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(dto);

        CartDto result = cartService.removeItemFromCart(listingId);

        assertNotNull(result);
        verify(cartItemRepository).delete(cartItem);
    }
}
