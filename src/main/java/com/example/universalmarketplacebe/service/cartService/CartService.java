package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.model.Cart;

public interface CartService {
    Cart getCart();

    Cart addItemToCart(Long listingId);

    Cart removeItemFromCart(Long listingId);
}
