package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.request.AddToCartRequest;
import com.example.universalmarketplacebe.dto.response.CartDto;

public interface CartService {
    CartDto getCart(String email);

    CartDto addItemToCart(AddToCartRequest addToCartRequest);

    CartDto removeItemFromCart(Long listingId);

    CartDto updateItemInCart(AddToCartRequest addToCartRequest);
    void checkout(String email, com.example.universalmarketplacebe.dto.request.CheckoutRequest request);

    void clearCart(String email);
}
