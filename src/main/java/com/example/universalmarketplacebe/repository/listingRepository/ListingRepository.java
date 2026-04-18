package com.example.universalmarketplacebe.repository.listingRepository;

import com.example.universalmarketplacebe.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    Page<Listing> findAllByAdvertiserId(Long advertiserId, Pageable pageable);
}
