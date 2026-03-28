package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.service.userService.UserService;
import lombok.RequiredArgsConstructor;
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
    public UserDto getUser() {
        return userService.getUser();
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
    public UserDto updateUser(@RequestBody UserUpdateRequest user) {
        return userService.updateUser(user);
    }

    /**
     * Zwraca wszystkie aktywne ogłoszenia (listings) konkretnego użytkownika.
     * Używane najczęściej w karcie podglądu publicznego profilu innej osoby.
     *
     * @param userId ID użytkownika-sprzedawcy.
     * @return Lista wszystkich jego ListingDto.
     */
    @GetMapping("/{userId}/listings")
    public List<ListingDto> getUserListings(@PathVariable Long userId) {
        return userService.getUserListings(userId);
    }

    /**
     * Zwraca listę ocen i recenzji, które inne osoby wystawiły temu konkretnemu użytkownikowi.
     *
     * @param userId ID recenzowanego użytkownika.
     * @return Lista ReviewDto.
     */
    @GetMapping("/{userId}/reviews")
    public List<ReviewDto> getUserReviews(@PathVariable Long userId) {
        return userService.getUserReviews(userId);
    }

}