package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public CartDto getCart() {
        return null;
    }

    @Override
    public CartDto addItemToCart(AddToCartRequest listingId) {
        return null;
    }

    @Override
    public CartDto removeItemFromCart(Long listingId) {
        return null;
    }

    @Override
    public CartDto updateItemInCart(AddToCartRequest addToCartRequest) {
        return null;
    }
}
