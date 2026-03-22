package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.model.Listening;
import com.example.universalmarketplacebe.repository.listingRepository.ListeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {
    private final ListeningRepository listeningRepository;

    @Override
    public List<Listening> getAllListings() {
        return List.of();
    }

    @Override
    public Listening getListingById(Long id) {
        return null;
    }

    @Override
    public Listening createListing(Listening listing) {
        return null;
    }

    @Override
    public Listening updateListing(Long id, Listening listing) {
        return null;
    }

    @Override
    public void deleteListing(Long id) {

    }
}
