package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.model.Listening;
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
    public List<Listening> getAllListings() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public Listening getListingById(@PathVariable Long id) {
        return listingService.getListingById(id);
    }

    @PostMapping
    public Listening createListing(@RequestBody Listening listing) {
        return listingService.createListing(listing);
    }

    @PutMapping("/{id}")
    public Listening updateListing(@PathVariable Long id, @RequestBody Listening listing) {
        return listingService.updateListing(id, listing);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

}
