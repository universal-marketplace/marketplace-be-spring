package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.PageResponse;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.service.userService.UserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
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
    @GetMapping("/me")
    public UserDto getUser(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return userService.getUser(principal.getEmail());
    }

    /**
     * Zwraca profil wskazanego po ID użytkownika (np. podgląd innego sprzedawcy).
     *
     * @param id ID profilu użytkownika.
     * <br>Przykładowe użycie: {@code GET /api/users/2}
     * @return Profil UserDto dla żądanego sprzedawcy.
     */
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
    @GetMapping("/{userId}/reviews")
    public PageResponse<ReviewDto> getUserReviews(
            @PathVariable Long userId,
            Pageable pageable) {
        return userService.getUserReviews(userId, pageable);
    }

}