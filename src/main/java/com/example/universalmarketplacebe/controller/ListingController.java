package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.PageResponse;
import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.service.listingService.ListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler zarządzający ogłoszeniami (ofertami sprzedaży / usługami).
 */
@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
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
    public PageResponse<ListingDto> getAllListings(Pageable pageable) {
        return listingService.getAllListings(pageable);
    }

    /**
     * Zwraca szczegóły pojedynczego ogłoszenia.
     *
     * @param id ID wybranego ogłoszenia.
     * <br>Przykładowe użycie: {@code GET /api/listings/1}
     * @return Obiekt ListingDto (zawierający m.in. dane sprzedawcy, cenę i tagi).
     */
    @GetMapping("/{id}")
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
     *   "currency": "PLN",
     *   "imageUrl": "https://example.com/bike.jpg",
     *   "tags": ["rowery", "sport"],
     *   "type": "PRODUCT"
     * }
     * </pre>
     * @return Zapisane ogłoszenie ListingDto z przydzielonym ID z bazy.
     */
    @PostMapping
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
     *   "currency": "PLN",
     *   "imageUrl": "https://example.com/bike.jpg",
     *   "tags": ["rowery", "sport", "promocja"],
     *   "type": "PRODUCT"
     * }
     * </pre>
     * @return Zaktualizowane ListingDto.
     */
    @PutMapping("/{id}")
    public ListingDto updateListing(@PathVariable Long id, @Valid @RequestBody ListingRequest listing) {
        return listingService.updateListing(id, listing);
    }

    /**
     * Miękko usuwa (lub fizycznie zależnie od configu hibernate) dane ogłoszenie z bazy.
     *
     * @param id ID wybranego ogłoszenia.
     * <br>Przykładowe użycie: {@code DELETE /api/listings/1}
     */
    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable Long id) {
        listingService.deleteListing(id);
    }

}