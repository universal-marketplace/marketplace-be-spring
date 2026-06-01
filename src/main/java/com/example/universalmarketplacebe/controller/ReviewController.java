package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.request.ReplyRequest;
import com.example.universalmarketplacebe.dto.request.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.response.ReviewDto;
import com.example.universalmarketplacebe.exception.ErrorResponse;
import com.example.universalmarketplacebe.service.reviewService.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler zarządzający opiniami (reviews) i odpowiedziami na nie.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Controller", description = "Endpoints for managing user reviews and replies")
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
    @Operation(summary = "Create review", description = "Creates a new review for another user")
    @ApiResponse(responseCode = "200", description = "Review created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Target user not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
     * <br>Przykładowe wywołanie: {@code POST /api/v1/reviews/10/reply} (pierwsza odpowiedź)
     * <br>Przykładowe wywołanie: {@code POST /api/v1/reviews/10/reply?idReply=5} (odpowiedź na inną odpowiedź)
     * @return Zaktualizowane ReviewDto.
     */
    @PostMapping("/{id}/reply")
    @Operation(summary = "Reply to review", description = "Adds a reply to an existing review")
    @ApiResponse(responseCode = "200", description = "Reply added successfully")
    @ApiResponse(responseCode = "404", description = "Review or parent reply not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
    @Operation(summary = "Update review", description = "Updates an existing review")
    @ApiResponse(responseCode = "200", description = "Review updated successfully")
    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ReviewDto updateReview(@PathVariable Long id, @Valid @RequestBody ReviewCreateRequest request) {
        return reviewService.updateReview(id, request);
    }

    /**
     * Usuwa recenzję. Tylko autor może usunąć.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete review", description = "Removes a review")
    @ApiResponse(responseCode = "200", description = "Review deleted successfully")
    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public void deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
    }

    /**
     * Aktualizuje treść odpowiedzi. Tylko autor może edytować.
     */
    @PutMapping("/replies/{replyId}")
    @Operation(summary = "Update reply", description = "Updates an existing reply")
    @ApiResponse(responseCode = "200", description = "Reply updated successfully")
    @ApiResponse(responseCode = "404", description = "Reply not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public ReviewDto updateReply(@PathVariable Long replyId, @Valid @RequestBody ReplyRequest request) {
        return reviewService.updateReply(replyId, request);
    }

    /**
     * Usuwa odpowiedź. Tylko autor może usunąć.
     */
    @DeleteMapping("/replies/{replyId}")
    @Operation(summary = "Delete reply", description = "Removes a reply")
    @ApiResponse(responseCode = "200", description = "Reply deleted successfully")
    @ApiResponse(responseCode = "404", description = "Reply not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public void deleteReply(@PathVariable Long replyId) {
        reviewService.deleteReply(replyId);
    }
}
