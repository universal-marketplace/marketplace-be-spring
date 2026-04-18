package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.PageResponse;
import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ListingMapper listingMapper;

    @InjectMocks
    private ListingServiceImpl listingService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    private User createMockUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        return user;
    }

    private ListingDto createMockListingDto(Long id) {
        return new ListingDto(
                id, "Title", "Description", BigDecimal.valueOf(100.0), "http://image.url",
                1L, "Advertiser", "avatar.png", 4.5, 10,
                List.of("tag1", "tag2"), "ITEM"
        );
    }

    private ListingRequest createMockListingRequest() {
        return new ListingRequest(
                "Title", "Description", BigDecimal.valueOf(100.0), "PLN",
                1, "http://image.url", List.of("tag1", "tag2"), "ITEM"
        );
    }

    @Test
    @DisplayName("getAllListings() - Happy Path")
    void getAllListings_HappyPath() {
        Pageable pageable = PageRequest.of(0, 10);
        Listing listing = new Listing();
        Page<Listing> page = new PageImpl<>(List.of(listing));
        ListingDto dto = createMockListingDto(1L);

        when(listingRepository.findAll(pageable)).thenReturn(page);
        when(listingMapper.toDto(any(Listing.class))).thenReturn(dto);

        PageResponse<ListingDto> result = listingService.getAllListings(pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(listingRepository).findAll(pageable);
    }

    @Test
    @DisplayName("getListingById() - Happy Path")
    void getListingById_HappyPath() {
        Long id = 1L;
        Listing listing = new Listing();
        ListingDto dto = createMockListingDto(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listingMapper.toDto(listing)).thenReturn(dto);

        ListingDto result = listingService.getListingById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    @DisplayName("createListing() - Happy Path")
    void createListing_HappyPath() {
        ListingRequest request = createMockListingRequest();
        User user = createMockUser();
        Listing listing = new Listing();
        ListingDto dto = createMockListingDto(1L);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(listingMapper.toEntity(request)).thenReturn(listing);
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        when(listingMapper.toDto(any(Listing.class))).thenReturn(dto);

        ListingDto result = listingService.createListing(request);

        assertNotNull(result);
        verify(listingRepository).save(any(Listing.class));
    }

    @Test
    @DisplayName("updateListing() - Happy Path")
    void updateListing_HappyPath() {
        Long id = 1L;
        ListingRequest request = createMockListingRequest();
        User user = createMockUser();
        Listing listing = new Listing();
        listing.setAdvertiser(user);
        ListingDto dto = createMockListingDto(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        when(listingMapper.toDto(any(Listing.class))).thenReturn(dto);

        ListingDto result = listingService.updateListing(id, request);

        assertNotNull(result);
        verify(listingRepository).save(listing);
        verify(listingMapper).updateEntityFromRequest(request, listing);
    }

    @Test
    @DisplayName("deleteListing() - Happy Path")
    void deleteListing_HappyPath() {
        Long id = 1L;
        User user = createMockUser();
        Listing listing = new Listing();
        listing.setAdvertiser(user);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);

        listingService.deleteListing(id);

        verify(listingRepository).delete(listing);
    }
}
