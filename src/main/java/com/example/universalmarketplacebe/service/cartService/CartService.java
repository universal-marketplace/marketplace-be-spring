package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;

public interface CartService {
    CartDto getCart();

    CartDto addItemToCart(AddToCartRequest listingId);

    CartDto removeItemFromCart(Long listingId);

    CartDto updateItemInCart(AddToCartRequest addToCartRequest);
}
