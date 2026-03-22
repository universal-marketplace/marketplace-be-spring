package com.example.universalmarketplacebe.repository.cartRepository;

import com.example.universalmarketplacebe.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
}
