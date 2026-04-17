package com.example.universalmarketplacebe.service.listingService;

import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    // Pomocnicza metoda do tworzenia ListingDto
    private ListingDto createMockListingDto(Long id) {
        return new ListingDto(
                id, "Title", "Description", "100.00 PLN", "http://image.url",
                1L, "Advertiser", "avatar.png", 4.5, 10,
                List.of("tag1", "tag2"), "ITEM"
        );
    }

    // Pomocnicza metoda do tworzenia ListingRequest
    private ListingRequest createMockListingRequest() {
        return new ListingRequest(
                "Title", "Description", BigDecimal.valueOf(100.0), "PLN",
                1, "http://image.url", List.of("tag1", "tag2"), "ITEM"
        );
    }

    // ==========================================
    // Tests for getAllListings()
    // ==========================================

    @Test
    @DisplayName("getAllListings() - Happy Path: Powinien zwrócić listę wszystkich ogłoszeń")
    void getAllListings_HappyPath_ReturnsList() {
        // Given
        List<Listing> mockListings = List.of(new Listing(), new Listing());
        List<ListingDto> expectedDtos = List.of(createMockListingDto(1L), createMockListingDto(2L));

        when(listingRepository.findAll()).thenReturn(mockListings);
        when(listingMapper.toDtoList(mockListings)).thenReturn(expectedDtos);

        // When
        List<ListingDto> result = listingService.getAllListings();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(listingRepository).findAll();
    }

    // ==========================================
    // Tests for getListingById(Long)
    // ==========================================

    @Test
    @DisplayName("getListingById() - Happy Path: Powinien zwrócić ogłoszenie o danym ID")
    void getListingById_HappyPath_ReturnsDto() {
        // Given
        Long id = 1L;
        Listing listing = new Listing();
        ListingDto expectedDto = createMockListingDto(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(listing));
        when(listingMapper.toDto(listing)).thenReturn(expectedDto);

        // When
        ListingDto result = listingService.getListingById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
    }

    @Test
    @DisplayName("getListingById() - Worse Path: Powinien rzucić wyjątek gdy ogłoszenie nie istnieje")
    void getListingById_WorsePath_NotFound() {
        when(listingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> listingService.getListingById(1L));
    }

    // ==========================================
    // Tests for createListing(ListingRequest)
    // ==========================================

    @Test
    @DisplayName("createListing() - Happy Path: Powinien stworzyć i zwrócić nowe ogłoszenie")
    void createListing_HappyPath_ReturnsDto() {
        // Given
        ListingRequest request = createMockListingRequest();
        Listing listing = new Listing();
        ListingDto expectedDto = createMockListingDto(1L);

        when(listingMapper.toEntity(request)).thenReturn(listing);
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        when(listingMapper.toDto(listing)).thenReturn(expectedDto);

        // When
        ListingDto result = listingService.createListing(request);

        // Then
        assertNotNull(result);
        verify(listingRepository).save(any(Listing.class));
    }

    @Test
    @DisplayName("createListing() - Edge Case: Powinien rzucić IllegalArgumentException gdy request to null")
    void createListing_EdgeCase_NullRequest() {
        assertThrows(IllegalArgumentException.class, () -> listingService.createListing(null));
    }

    // ==========================================
    // Tests for updateListing(Long, ListingRequest)
    // ==========================================

    @Test
    @DisplayName("updateListing() - Happy Path: Powinien zaktualizować istniejące ogłoszenie")
    void updateListing_HappyPath_ReturnsUpdatedDto() {
        // Given
        Long id = 1L;
        ListingRequest request = createMockListingRequest();
        Listing existingListing = new Listing();
        ListingDto expectedDto = createMockListingDto(id);

        when(listingRepository.findById(id)).thenReturn(Optional.of(existingListing));
        when(listingRepository.save(any(Listing.class))).thenReturn(existingListing);
        when(listingMapper.toDto(existingListing)).thenReturn(expectedDto);

        // When
        ListingDto result = listingService.updateListing(id, request);

        // Then
        assertNotNull(result);
        verify(listingRepository).save(existingListing);
    }

    @Test
    @DisplayName("updateListing() - Worse Path: Powinien rzucić wyjątek przy próbie edycji nieistniejącego ogłoszenia")
    void updateListing_WorsePath_NotFound() {
        when(listingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> listingService.updateListing(1L, createMockListingRequest()));
    }

    // ==========================================
    // Tests for deleteListing(Long)
    // ==========================================

    @Test
    @DisplayName("deleteListing() - Happy Path: Powinien usunąć istniejące ogłoszenie")
    void deleteListing_HappyPath_Success() {
        // Given
        Long id = 1L;
        when(listingRepository.existsById(id)).thenReturn(true);

        // When
        listingService.deleteListing(id);

        // Then
        verify(listingRepository).deleteById(id);
    }

    @Test
    @DisplayName("deleteListing() - Worse Path: Powinien rzucić wyjątek przy próbie usunięcia nieistniejącego ogłoszenia")
    void deleteListing_WorsePath_NotFound() {
        when(listingRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> listingService.deleteListing(1L));
    }

    @Test
    @DisplayName("deleteListing() - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null")
    void deleteListing_EdgeCase_NullId() {
        assertThrows(IllegalArgumentException.class, () -> listingService.deleteListing(null));
    }
}