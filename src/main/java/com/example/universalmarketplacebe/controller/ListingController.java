package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.service.listingService.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {
    private final ListingService listingService;

    @GetMapping
    public List<ListingDto> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public ListingDto getListingById(@PathVariable Long id) {
        return listingService.getListingById(id);
    }

    @PostMapping
    public ListingDto createListing(@RequestBody ListingRequest listing) {
        return listingService.createListing(listing);
    }

    @PutMapping("/{id}")
    public ListingDto updateListing(@PathVariable Long id, @RequestBody ListingRequest listing) {
        return listingService.updateListing(id, listing);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

}
