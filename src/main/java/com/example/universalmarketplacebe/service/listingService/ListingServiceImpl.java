package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.Type;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listeningRepository;
    private final ListingMapper listingMapper;

    @Override
    public List<ListingDto> getAllListings() {
        return listeningRepository.findAll().stream()
                .map(listingMapper::toDto)
                .toList();
    }

    @Override
    public ListingDto getListingById(Long id) {
        return listingMapper.toDto(
                listeningRepository.findById(id)
                .orElseThrow(
                        () -> new IllegalArgumentException("Listing not found")
                ));
    }

    @Override
    public ListingDto createListing(ListingRequest listing) {
        return listingMapper.toDto(
                listeningRepository.save(
                        listingMapper.toEntity(listing)
                )
        );
    }

    @Override
    @Transactional
    public ListingDto updateListing(Long id, ListingRequest listing) {
        Listing listingById = listeningRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Listing not found")
        );
        listingById.setTitle(listing.title());
        listingById.setDescription(listing.description());
        listingById.setPrice(listing.priceAmount());
        listingById.setUnitAmount(listing.unitAmount());
        listingById.setCurrency(listing.currency());
        listingById.setImageUrl(listing.imageUrl());
        listingById.getTags().addAll(listing.tags());
        listingById.setType(Type.valueOf(listing.type()));

        return listingMapper.toDto(listingById);
    }

    @Override
    public void deleteListing(Long id) {
        listeningRepository.deleteById(id);
    }
}
