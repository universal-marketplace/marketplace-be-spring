package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.PageResponse;
import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;

    @Override
    public PageResponse<ListingDto> getAllListings(Pageable pageable) {
        Page<Listing> listingPage = listingRepository.findAll(pageable);
        return new PageResponse<>(listingPage.map(listingMapper::toDto));
    }

    @Override
    public ListingDto getListingById(Long id) {
        return listingMapper.toDto(
                listingRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Listing not found"))
        );
    }

    @Override
    @Transactional
    public ListingDto createListing(ListingRequest listing) {
        if (listing == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        User advertiser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Listing entity = listingMapper.toEntity(listing);
        entity.setAdvertiser(advertiser);

        return listingMapper.toDto(listingRepository.save(entity));
    }

    @Override
    @Transactional
    public ListingDto updateListing(Long id, ListingRequest listing) {
        Listing existingListing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!existingListing.getAdvertiser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to update this listing");
        }

        listingMapper.updateEntityFromRequest(listing, existingListing);
        return listingMapper.toDto(listingRepository.save(existingListing));
    }

    @Override
    @Transactional
    public void deleteListing(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!listing.getAdvertiser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not authorized to delete this listing");
        }

        listingRepository.delete(listing);
    }
}
