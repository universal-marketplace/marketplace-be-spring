package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.mapper.CartMapper;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.repository.cartItemRepository.CartItemRepository;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ListingRepository listingRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;

    @Override
    public CartDto getCart(String email) {
        return null;
    }

    @Override
    public CartDto addItemToCart(AddToCartRequest addToCartRequest) {
        Listing listing = listingRepository.findById(addToCartRequest.listingId()).orElseThrow(
                () -> new RuntimeException("xyz")
        );

        getCart("ok");
        Cart cart = new Cart();
        //podpbna logika tylko chcę otrzymać tylko Cart a nie zmieniać na CartDto

        Optional<CartItem> cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing);

        if (cartItem.isPresent()) {
            cartItem.get().setQuantity(addToCartRequest.quantity());
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setListing(listing);
            newCartItem.setQuantity(addToCartRequest.quantity());
            cartItemRepository.save(newCartItem);
        }

        return cartMapper.toDto(cart);
    }

    @Override
    public CartDto removeItemFromCart(Long listingId) {
        Listing listing = listingRepository.findById(listingId).orElseThrow(
                () -> new RuntimeException("xyz")
        );

        //getCart(principal.getEmail());
        Cart cart = new Cart();
        //podpbna logika tylko chcę otrzymać tylko Cart a nie zmieniać na CartDto

        CartItem cartItem = cartItemRepository.findCartItemByCartAndListing(cart, listing)
                .orElseThrow(
                        () -> new RuntimeException("xyz")
                );
        cartItemRepository.delete(cartItem);

        return cartMapper.toDto(cart);
    }

    @Override
    public CartDto updateItemInCart(AddToCartRequest addToCartRequest) {
        return null;
    }
}
