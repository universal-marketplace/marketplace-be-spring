package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.service.reviewService.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler zarządzający opiniami (reviews) i odpowiedziami na nie.
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Zalogowany użytkownik wystawia nową opinię drugiemu sprzedawcy/kupującemu.
     * Używane np. po finalizacji transakcji.
     *
     * @param request DTO z id ocenianego użytkownika, gwiazdkami (1-5) i treścią.
     * <br>Przykładowy Payload (żądanie z body w trybie JSON - warto tu dodać \@RequestBody):
     * <pre>
     * {
     *   "targetUserId": 2,
     *   "rating": 5,
     *   "comment": "Bardzo szybka wysyłka, kontakt pierwsza klasa!"
     * }
     * </pre>
     * @return Zapisany rekord w formie ReviewDto.
     */
    @PostMapping
    public ReviewDto createReview(@Valid @RequestBody ReviewCreateRequest request) {
        return reviewService.createReview(request);
    }

    /**
     * Odpowiadanie na cudzą recenzję własnego profilu.
     * Użytkownik, który otrzymał recenzję, dodaje do niej tzw. "reply".
     *
     * @param id ID docelowej recenzji.
     * @param idReply ID nadrzędnej odpowiedzi (opcjonalne - używane tylko przy tworzeniu wątku odpowiedzi).
     * @param replyRequest Treść odpowiedzi.
     * <br>Przykładowe wywołanie: {@code POST /api/reviews/10/reply} (pierwsza odpowiedź)
     * <br>Przykładowe wywołanie: {@code POST /api/reviews/10/reply?idReply=5} (odpowiedź na inną odpowiedź)
     * @return Zaktualizowane ReviewDto.
     */
    @PostMapping("/{id}/reply")
    public ReviewDto replyToReview(
            @PathVariable Long id,
            @RequestParam(required = false) Long idReply,
            @Valid @RequestBody ReplyRequest replyRequest) {
        return reviewService.replyToReview(id, idReply, replyRequest);
    }

    /**
     * Aktualizuje treść i ocenę recenzji. Tylko autor może edytować.
     */
    @PutMapping("/{id}")
    public ReviewDto updateReview(@PathVariable Long id, @Valid @RequestBody ReviewCreateRequest request) {
        return reviewService.updateReview(id, request);
    }

    /**
     * Usuwa recenzję. Tylko autor może usunąć.
     */
    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    /**
     * Aktualizuje treść odpowiedzi. Tylko autor może edytować.
     */
    @PutMapping("/replies/{replyId}")
    public ReviewDto updateReply(@PathVariable Long replyId, @Valid @RequestBody ReplyRequest request) {
        return reviewService.updateReply(replyId, request);
    }

    /**
     * Usuwa odpowiedź. Tylko autor może usunąć.
     */
    @DeleteMapping("/replies/{replyId}")
    public void deleteReply(@PathVariable Long replyId) {
        reviewService.deleteReply(replyId);
    }
}
