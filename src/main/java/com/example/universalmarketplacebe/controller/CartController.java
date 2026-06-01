package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.request.AddToCartRequest;
import com.example.universalmarketplacebe.dto.response.CartDto;
import com.example.universalmarketplacebe.dto.request.CheckoutRequest;
import com.example.universalmarketplacebe.exception.ErrorResponse;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.service.cartService.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart Controller", description = "Endpoints for managing the shopping cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/checkout")
    @Operation(summary = "Checkout", description = "Finalizes the purchase and creates an order")
    @ApiResponse(responseCode = "200", description = "Checkout successful")
    public void checkout(Authentication authentication, @Valid @RequestBody CheckoutRequest request) {
        User principal = (User) authentication.getPrincipal();
        cartService.checkout(principal.getEmail(), request);
    }

    @GetMapping
    @Operation(summary = "Get cart", description = "Returns the contents of the current user's shopping cart")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cart")
    public CartDto getCart(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return cartService.getCart(principal.getEmail());
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart", description = "Adds a listing to the shopping cart")
    public CartDto addItemToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        return cartService.addItemToCart(addToCartRequest);
    }

    @PutMapping("/items")
    @Operation(summary = "Update item in cart", description = "Updates the quantity or details of an item in the cart")
    public CartDto updateItemInCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        return cartService.updateItemInCart(addToCartRequest);
    }

    @DeleteMapping("/items/{listingId}")
    @Operation(summary = "Remove item from cart", description = "Removes an item from the shopping cart")
    public CartDto removeItemFromCart(@PathVariable Long listingId) {
        return cartService.removeItemFromCart(listingId);
    }

    @DeleteMapping
    @Operation(summary = "Clear cart", description = "Removes all items from the shopping cart")
    public void clearCart(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        cartService.clearCart(principal.getEmail());
    }
}
