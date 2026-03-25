package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;

import java.util.List;

public interface ListingService {
    List<ListingDto> getAllListings();

    ListingDto getListingById(Long id);

    ListingDto createListing(ListingRequest listing);

    ListingDto updateListing(Long id, ListingRequest listing);

    void deleteListing(Long id);
}
