package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.response.PageResponse;
import com.example.universalmarketplacebe.dto.request.ListingRequest;
import com.example.universalmarketplacebe.dto.response.ListingDto;
import com.example.universalmarketplacebe.exception.ErrorResponse;
import com.example.universalmarketplacebe.service.listingService.ListingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler zarządzający ogłoszeniami (ofertami sprzedaży / usługami).
 */
@RestController
@RequestMapping("/api/v1/listings")
@RequiredArgsConstructor
@Tag(name = "Listing Controller", description = "Endpoints for managing marketplace listings")
public class ListingController {
    private final ListingService listingService;

    /**
     * Zwraca listę wszystkich ogłoszeń w serwisie ze stronicowaniem.
     * Używane najczęściej do przeglądania ogólnego np. na stronie głównej.
     *
     * @param pageable Parametry stronicowania.
     * @return PageResponse ListingDto reprezentujących szczegóły ofert.
     */
    @GetMapping
    @Operation(summary = "Get all listings", description = "Returns a paginated list of all marketplace listings")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved listings")
    public PageResponse<ListingDto> getAllListings(Pageable pageable) {
        return listingService.getAllListings(pageable);
    }

    /**
     * Zwraca szczegóły pojedynczego ogłoszenia.
     *
     * @param id ID wybranego ogłoszenia.
     * <br>Przykładowe użycie: {@code GET /api/v1/listings/1}
     * @return Obiekt ListingDto (zawierający m.in. dane sprzedawcy, cenę i tagi).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get listing by ID", description = "Returns details of a single listing")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved listing")
    @ApiResponse(responseCode = "404", description = "Listing not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ListingDto getListingById(@PathVariable Long id) {
        return listingService.getListingById(id);
    }

    /**
     * Tworzy nowe ogłoszenie podpięte pod aktualnie zalogowanego użytkownika.
     *
     * @param listing DTO z danymi tworzonego ogłoszenia.
     * <br>Przykładowy Payload:
     * <pre>
     * {
     *   "title": "Sprzedam Rower Męski",
     *   "description": "Rower w bardzo dobrym stanie technicznym.",
     *   "priceAmount": 550.00,
     *   "imageUrl": "https://example.com/bike.jpg",
     *   "tags": ["rowery", "sport"],
     *   "type": "PRODUCT"
     * }
     * </pre>
     * @return Zapisane ogłoszenie ListingDto z przydzielonym ID z bazy.
     */
    @PostMapping
    @Operation(summary = "Create listing", description = "Creates a new listing for the currently authenticated user")
    @ApiResponse(responseCode = "201", description = "Listing created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ListingDto createListing(@Valid @RequestBody ListingRequest listing) {
        return listingService.createListing(listing);
    }

    /**
     * Aktualizuje istniejące ogłoszenie na podstawie podanego ID (np. zmiana ceny, opisu).
     *
     * @param id ID ogłoszenia, które ma zostać zaktualizowane.
     * @param listing Dane docelowe (nowe) ogłoszenia.
     * <br>Przykładowy Payload:
     * <pre>
     * {
     *   "title": "Sprzedam Rower Męski (Zaktualizowane)",
     *   "description": "Rower w obniżonej cenie!",
     *   "priceAmount": 450.00,
     *   "imageUrl": "https://example.com/bike.jpg",
     *   "tags": ["rowery", "sport", "promocja"],
     *   "type": "PRODUCT"
     * }
     * </pre>
     * @return Zaktualizowane ListingDto.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update listing", description = "Updates an existing listing")
    @ApiResponse(responseCode = "200", description = "Listing updated successfully")
    @ApiResponse(responseCode = "404", description = "Listing not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ListingDto updateListing(@PathVariable Long id, @Valid @RequestBody ListingRequest listing) {
        return listingService.updateListing(id, listing);
    }

    /**
     * Miękko usuwa (lub fizycznie zależnie od configu hibernate) dane ogłoszenie z bazy.
     *
     * @param id ID wybranego ogłoszenia.
     * <br>Przykładowe użycie: {@code DELETE /api/v1/listings/1}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete listing", description = "Removes a listing from the marketplace")
    @ApiResponse(responseCode = "200", description = "Listing deleted successfully")
    @ApiResponse(responseCode = "404", description = "Listing not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

}