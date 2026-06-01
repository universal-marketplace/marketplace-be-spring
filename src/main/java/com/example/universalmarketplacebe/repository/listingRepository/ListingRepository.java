package com.example.universalmarketplacebe.repository.listingRepository;

import com.example.universalmarketplacebe.model.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long> {
    @org.springframework.data.jpa.repository.Query("SELECT l FROM Listing l WHERE l.type = com.example.universalmarketplacebe.model.Type.SERVICE OR (l.type = com.example.universalmarketplacebe.model.Type.ITEM AND l.unitAmount > 0)")
    Page<Listing> findAllAvailable(Pageable pageable);
    @org.springframework.data.jpa.repository.Query("SELECT l FROM Listing l WHERE l.advertiser.id = :advertiserId AND (l.type = com.example.universalmarketplacebe.model.Type.SERVICE OR (l.type = com.example.universalmarketplacebe.model.Type.ITEM AND l.unitAmount > 0))")
    Page<Listing> findAllAvailableByAdvertiserId(Long advertiserId, Pageable pageable);
    Page<Listing> findAllByAdvertiserId(Long advertiserId, Pageable pageable);
    List<Listing> findAllByAdvertiserId(Long advertiserId);
}
