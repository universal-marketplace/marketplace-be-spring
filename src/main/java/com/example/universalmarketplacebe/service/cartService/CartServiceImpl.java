package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.mapper.CartMapper;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.cartItemRepository.CartItemRepository;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ListingRepository listingRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDto getCart(String email) {
        Cart cart = getOrCreateCart(email);
        updateCartTotalPrice(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public CartDto addItemToCart(AddToCartRequest addToCartRequest) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(addToCartRequest.listingId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        Optional<CartItem> cartItemOpt = cartItemRepository.findCartItemByCartAndListing(cart, listing);

        if (cartItemOpt.isPresent()) {
            CartItem cartItem = cartItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequest.quantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setListing(listing);
            newCartItem.setQuantity(addToCartRequest.quantity());
            cartItemRepository.save(newCartItem);
            cart.getCartItems().add(newCartItem);
        }

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto removeItemFromCart(Long listingId) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        CartItem cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDto updateItemInCart(AddToCartRequest addToCartRequest) {
        String email = getCurrentUserEmail();
        Cart cart = getOrCreateCart(email);
        Listing listing = listingRepository.findById(addToCartRequest.listingId())
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));

        CartItem cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing)
                .orElseThrow(() -> new IllegalArgumentException("Item not found in cart"));

        if (addToCartRequest.quantity() <= 0) {
            cart.getCartItems().remove(cartItem);
            cartItemRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(addToCartRequest.quantity());
            cartItemRepository.save(cartItem);
        }

        updateCartTotalPrice(cart);
        return cartMapper.toDto(cartRepository.save(cart));
    }

    private Cart getOrCreateCart(String email) {
        return cartRepository.findByUserEmail(email).orElseGet(() -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartItems(new ArrayList<>());
            newCart.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(newCart);
        });
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void updateCartTotalPrice(Cart cart) {
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getListing().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(total);
    }
}
