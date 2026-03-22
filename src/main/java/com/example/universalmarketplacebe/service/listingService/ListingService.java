package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.model.Listening;

import java.util.List;

public interface ListingService {
    List<Listening> getAllListings();

    Listening getListingById(Long id);

    Listening createListing(Listening listing);

    Listening updateListing(Long id, Listening listing);

    void deleteListing(Long id);
}
