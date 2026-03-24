package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.repository.listingRepository.ListeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListeningRepository listeningRepository;

    @Override
    public List<ListingDto> getAllListings() {
        return List.of();
    }

    @Override
    public ListingDto getListingById(Long id) {
        return null;
    }

    @Override
    public ListingDto createListing(ListingRequest listing) {
        return null;
    }

    @Override
    public ListingDto updateListing(Long id, ListingRequest listing) {
        return null;
    }

    @Override
    public void deleteListing(Long id) {

    }
}
