package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.response.PageResponse;
import com.example.universalmarketplacebe.dto.request.ListingRequest;
import com.example.universalmarketplacebe.dto.response.ListingDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ListingService {
    PageResponse<ListingDto> getAllListings(Pageable pageable);

    ListingDto getListingById(Long id);

    ListingDto createListing(ListingRequest listing);

    ListingDto updateListing(Long id, ListingRequest listing);

    void deleteListing(Long id);
}
