package com.example.universalmarketplacebe.repository.orderRepository;

import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.Order;
import com.example.universalmarketplacebe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByBuyerOrderByCreatedAtDesc(User buyer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.items i WHERE i.listing.advertiser = :seller ORDER BY o.createdAt DESC")
    List<Order> findAllBySeller(@Param("seller") User seller);

    @Query("SELECT COUNT(i) > 0 FROM Order o JOIN o.items i WHERE i.listing = :listing AND i.bookingDate = :date AND o.status != 'CANCELLED'")
    boolean existsByListingAndBookingDate(@Param("listing") Listing listing, @Param("date") java.time.LocalDate date);
}
