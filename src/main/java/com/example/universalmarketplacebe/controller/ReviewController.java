package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.reviewRequest.ReplyRequest;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.service.reviewService.ReviewService;
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
    public ReviewDto createReview(@RequestBody ReviewCreateRequest request) {
        return reviewService.createReview(request);
    }

    /**
     * Odpowiadanie na cudzą recenzję własnego profilu.
     * Użytkownik, który otrzymał recenzję, dodaje do niej tzw. "reply".
     *
     * @param id ID docelowej recenzji.
     * @param idReply ID docelowej odpowiedzi.
     * @param replyRequest Treść odpowiedzi.
     * <br>Przykładowe wywołanie: {@code POST /api/reviews/10/reply}
     * <br>Przykładowy Payload:
     * <pre>
     * {
     *   "reply": "Dziękuję za zakupy, polecam się na przyszłość!"
     * }
     * </pre>
     * @return Zaktualizowane ReviewDto, które zagnieżdża w sobie najnowszą odpowiedź.
     */
    @PostMapping("/{id}/reply/{idReply}")
    public ReviewDto replyToReview(@PathVariable Long id,@PathVariable Long idReply, @RequestBody ReplyRequest replyRequest) {
        return reviewService.replyToReview(id, idReply, replyRequest);
    }
}
