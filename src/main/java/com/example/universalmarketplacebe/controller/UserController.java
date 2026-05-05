package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.response.PageResponse;
import com.example.universalmarketplacebe.dto.response.ListingDto;
import com.example.universalmarketplacebe.dto.response.ReviewDto;
import com.example.universalmarketplacebe.dto.request.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.response.UserDto;
import com.example.universalmarketplacebe.exception.ErrorResponse;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kontroler odpowiedzialny za zarządzanie danymi i profilami użytkowników.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController {

    private final UserService userService;

    /**
     * Zwraca dane profilu zalogowanego użytkownika (np. do strony /profil w aplikacji frontendowej).
     *
     * @return UserDto (ID, imię, email, statystyki opinii).
     * <br>Przykładowe dane zwrotne:
     * <pre>
     * {
     *   "id": 1,
     *   "name": "Jan Kowalski",
     *   "email": "jan@example.com",
     *   "avatarUrl": "https://example.com/avatar.png",
     *   "description": "Sprzedawca w miarę uczciwy.",
     *   "rating": 4.5,
     *   "reviewCount": 12
     * }
     * </pre>
     */
    @Operation(summary = "Get current user profile", description = "Returns the profile of the currently authenticated user based on the JWT token.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile")
    @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token is missing or invalid", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/me")
    public UserDto getUser(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return userService.getUser(principal.getEmail());
    }

    /**
     * Zwraca profil wskazanego po ID użytkownika (np. podgląd innego sprzedawcy).
     *
     * @param id ID profilu użytkownika.
     * <br>Przykładowe użycie: {@code GET /api/v1/users/2}
     * @return Profil UserDto dla żądanego sprzedawcy.
     */
    @Operation(summary = "Get user profile by ID", description = "Returns the profile of a user specified by their ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    /**
     * Pozwala zaktualizować informacje własnego (zalogowanego) profilu.
     *
     * @param user Aktualne dane wpisane przez użytkownika na frontendzie.
     * <br>Przykładowy Payload (żądanie):
     * <pre>
     * {
     *   "name": "Janusz Biznesu",
     *   "email": "nowy_email@example.com",
     *   "oldPassword": "...",
     *   "avatarUrl": "https://example.com/new_avatar.png",
     *   "description": "Zmieniony opis o mnie."
     * }
     * </pre>
     * @return Zaktualizowany UserDto, który potem nadpisze store na frontendzie.
     */
    @Operation(summary = "Update current user profile", description = "Updates the profile of the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "Successfully updated user profile")
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/me")
    public UserDto updateUser(Authentication authentication, @Valid @RequestBody UserUpdateRequest user) {
        User principal = (User) authentication.getPrincipal();
        return userService.updateUser(principal.getEmail(), user);
    }

    /**
     * Zwraca wszystkie aktywne ogłoszenia (listings) konkretnego użytkownika.
     * Wspiera stronicowanie i sortowanie.
     *
     * @param userId ID użytkownika-sprzedawcy.
     * @param pageable Parametry stronicowania (page, size, sort).
     * @return PageResponse z listą ListingDto.
     */
    @Operation(summary = "Get user listings", description = "Returns a paginated list of listings created by a specific user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved listings")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{userId}/listings")
    public PageResponse<ListingDto> getUserListings(
            @PathVariable Long userId,
            Pageable pageable) {
        return userService.getUserListings(userId, pageable);
    }

    /**
     * Zwraca listę ocen i recenzji, które inne osoby wystawiły temu konkretnemu użytkownikowi.
     * Wspiera stronicowanie i sortowanie.
     *
     * @param userId ID recenzowanego użytkownika.
     * @param pageable Parametry stronicowania (page, size, sort).
     * @return PageResponse z listą ReviewDto.
     */
    @Operation(summary = "Get user reviews", description = "Returns a paginated list of reviews received by a specific user.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved reviews")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/{userId}/reviews")
    public PageResponse<ReviewDto> getUserReviews(
            @PathVariable Long userId,
            Pageable pageable) {
        return userService.getUserReviews(userId, pageable);
    }

}