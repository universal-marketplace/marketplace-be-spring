package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {
    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ListingMapper listingMapper;

    @InjectMocks
    private ListingServiceImpl listingService;

}