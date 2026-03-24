package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
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
    public CartDto getCart() {
        return cartService.getCart();
    }

    @PostMapping("/items/")
    public CartDto addItemToCart(@RequestBody AddToCartRequest addToCartRequest) {
        return cartService.addItemToCart(addToCartRequest);
    }

    @PutMapping("/items/")
    public CartDto updateItemInCart(@RequestBody AddToCartRequest addToCartRequest) {
        return cartService.updateItemInCart(addToCartRequest);
    }

    @DeleteMapping("/items/{listingId}")
    public CartDto removeItemFromCart(@PathVariable Long listingId) {
        return cartService.removeItemFromCart(listingId);
    }

}
