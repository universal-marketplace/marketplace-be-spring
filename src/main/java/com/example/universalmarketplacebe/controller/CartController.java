package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.service.cartService.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Cart getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items/{listingId}")
    public Cart addItemToCart(@PathVariable Long listingId) {
        return cartService.addItemToCart(listingId);
    }

    @DeleteMapping("/items/{listingId}")
    public Cart removeItemFromCart(@PathVariable Long listingId) {
        return cartService.removeItemFromCart(listingId);
    }

}
