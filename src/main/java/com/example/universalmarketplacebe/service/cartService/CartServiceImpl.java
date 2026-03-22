package com.example.universalmarketplacebe.service.cartService;

import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.repository.cartRepository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Override
    public Cart getCart() {
        return null;
    }

    @Override
    public Cart addItemToCart(Long listingId) {
        return null;
    }

    @Override
    public Cart removeItemFromCart(Long listingId) {
        return null;
    }
}
