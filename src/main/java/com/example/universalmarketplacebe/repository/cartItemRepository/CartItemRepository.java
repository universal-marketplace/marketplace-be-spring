package com.example.universalmarketplacebe.repository.cartItemRepository;

import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import com.example.universalmarketplacebe.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findCartItemByCartAndListing(Cart cart, Listing listing);
}
