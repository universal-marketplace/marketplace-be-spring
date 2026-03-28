package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.dto.cartResponse.CartItemDto;
import com.example.universalmarketplacebe.mapper.CartMapper;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private CartMapper cartMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    // Pomocnicza metoda do tworzenia CartDto
    private CartDto createMockCartDto() {
        CartItemDto item = new CartItemDto(1L, "Listing Title", new BigDecimal("100.00"), 1, new BigDecimal("100.00"));
        return new CartDto(List.of(item), new BigDecimal("100.00"));
    }

    // ==========================================
    // Tests for getCart()
    // ==========================================

    @Test
    @DisplayName("getCart() - Happy Path: Powinien zwrócić koszyk aktualnego użytkownika")
    void getCart_HappyPath_ReturnsCartDto() {
        // Given
        Cart mockCart = new Cart();
        CartDto expectedDto = createMockCartDto();

        // W serwisie pewnie będziesz pobierał koszyk dla zalogowanego usera
        when(cartRepository.findByUserEmail(anyString())).thenReturn(Optional.of(mockCart));
        when(cartMapper.toDto(mockCart)).thenReturn(expectedDto);

        // When
        CartDto result = cartService.getCart();

        // Then
        assertNotNull(result);
        assertEquals(expectedDto.totalPrice(), result.totalPrice());
    }

    @Test
    @DisplayName("getCart() - Worse Path: Powinien rzucić wyjątek gdy użytkownik nie ma jeszcze koszyka")
    void getCart_WorsePath_NotFound() {
        when(cartRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cartService.getCart());
    }

    // ==========================================
    // Tests for addItemToCart(AddToCartRequest)
    // ==========================================

    @Test
    @DisplayName("addItemToCart() - Happy Path: Powinien dodać produkt do koszyka")
    void addItemToCart_HappyPath_AddsItem() {
        // Given
        AddToCartRequest request = new AddToCartRequest(1L, 1);
        Cart cart = new Cart();
        CartDto expectedDto = createMockCartDto();

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(expectedDto);

        // When
        CartDto result = cartService.addItemToCart(request);

        // Then
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("addItemToCart() - Edge Case: Powinien rzucić IllegalArgumentException dla ujemnej ilości")
    void addItemToCart_EdgeCase_NegativeQuantity() {
        AddToCartRequest request = new AddToCartRequest(1L, -5);
        assertThrows(IllegalArgumentException.class, () -> cartService.addItemToCart(request));
    }

    // ==========================================
    // Tests for removeItemFromCart(Long)
    // ==========================================

    @Test
    @DisplayName("removeItemFromCart() - Happy Path: Powinien usunąć produkt z koszyka")
    void removeItemFromCart_HappyPath_RemovesItem() {
        // Given
        Long listingId = 1L;
        Cart cart = new Cart();
        CartDto expectedDto = createMockCartDto();

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(expectedDto);

        // When
        CartDto result = cartService.removeItemFromCart(listingId);

        // Then
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("removeItemFromCart() - Edge Case: Powinien rzucić IllegalArgumentException gdy ID to null")
    void removeItemFromCart_EdgeCase_NullId() {
        assertThrows(IllegalArgumentException.class, () -> cartService.removeItemFromCart(null));
    }

    // ==========================================
    // Tests for updateItemInCart(AddToCartRequest)
    // ==========================================

    @Test
    @DisplayName("updateItemInCart() - Happy Path: Powinien zaktualizować ilość produktu")
    void updateItemInCart_HappyPath_UpdatesQuantity() {
        // Given
        AddToCartRequest request = new AddToCartRequest(1L, 5);
        Cart cart = new Cart();
        CartDto expectedDto = createMockCartDto();

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartMapper.toDto(cart)).thenReturn(expectedDto);

        // When
        CartDto result = cartService.updateItemInCart(request);

        // Then
        assertNotNull(result);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("updateItemInCart() - Worse Path: Powinien rzucić wyjątek gdy produktu nie ma w koszyku")
    void updateItemInCart_WorsePath_ItemNotInCart() {
        // Given
        AddToCartRequest request = new AddToCartRequest(999L, 1);
        
        // When & Then
        assertThrows(RuntimeException.class, () -> cartService.updateItemInCart(request));
    }
}
