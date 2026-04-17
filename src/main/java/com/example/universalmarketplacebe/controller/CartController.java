package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.cartRequest.AddToCartRequest;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.service.cartService.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler obsługujący operacje na koszyku zalogowanego użytkownika.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Zwraca zawartość koszyka aktualnie zalogowanego użytkownika.
     *
     * @return CartDto z listą przedmiotów i całkowitym podsumowaniem kosztów.
     * <br>Przykładowy zwrot:
     * <pre>
     * {
     *   "id": 1,
     *   "userId": 1,
     *   "items": [
     *     {
     *       "id": 1,
     *       "listingId": 10,
     *       "listingTitle": "Laptop",
     *       "quantity": 1,
     *       "price": "2000.00 PLN"
     *     }
     *   ],
     *   "totalPrice": "2000.00 PLN"
     * }
     * </pre>
     */
    @GetMapping
    public CartDto getCart(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return cartService.getCart(principal.getEmail());
    }

    /**
     * Dodaje przedmiot do koszyka użytkownika. Jeśli przedmiot już tam jest, zwiększa ilość.
     *
     * @param addToCartRequest DTO z informacjami o dodawanym przedmiocie.
     * <br>Przykładowy Payload:
     * <pre>
     * {
     *   "listingId": 10,
     *   "quantity": 1
     * }
     * </pre>
     * @return Zaktualizowany CartDto.
     */
    @PostMapping("/items/")
    public CartDto addItemToCart(@RequestBody AddToCartRequest addToCartRequest) {
        return cartService.addItemToCart(addToCartRequest);
    }

    /**
     * Aktualizuje ilość danego przedmiotu w koszyku.
     * Jeśli ilość spadnie do 0 (lub poniżej), można rozważyć usunięcie przedmiotu (zależnie od implementacji).
     *
     * @param addToCartRequest DTO zawierające ID ogłoszenia i nową docelową ilość.
     * <br>Przykładowy Payload:
     * <pre>
     * {
     *   "listingId": 10,
     *   "quantity": 2
     * }
     * </pre>
     * @return Zaktualizowany CartDto.
     */
    @PutMapping("/items/")
    public CartDto updateItemInCart(@RequestBody AddToCartRequest addToCartRequest) {
        return cartService.updateItemInCart(addToCartRequest);
    }

    /**
     * Całkowicie usuwa wybrany przedmiot z koszyka na podstawie ID ogłoszenia.
     *
     * @param listingId ID ogłoszenia do usunięcia.
     * <br>Przykładowe użycie: {@code DELETE /api/cart/items/10}
     * @return Zaktualizowany CartDto (bez usuniętego przedmiotu).
     */
    @DeleteMapping("/items/{listingId}")
    public CartDto removeItemFromCart(@PathVariable Long listingId) {
        return cartService.removeItemFromCart(listingId);
    }

}