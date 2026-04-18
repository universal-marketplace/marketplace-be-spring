package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.cartResponse.CartDto;
import com.example.universalmarketplacebe.dto.cartResponse.CartItemDto;
import com.example.universalmarketplacebe.model.Cart;
import com.example.universalmarketplacebe.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(config = MapperConfig.class)
public interface CartMapper {

    /**
     * Mapuje główną encję koszyka na CartDto.
     */
    @Mapping(target = "items", source = "cartItems")
    CartDto toDto(Cart cart);

    /**
     * Mapuje element koszyka (CartItem) na CartItemDto.
     * Wyciąga dane bezpośrednio z powiązanego ogłoszenia (Listing).
     */
    @Mapping(target = "listingId", source = "listing.id")
    @Mapping(target = "title", source = "listing.title")
    @Mapping(target = "unitPrice", source = "listing.price")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "subtotal", expression = "java(cartItem.getListing().getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity())))")
    CartItemDto toItemDto(CartItem cartItem);

    /**
     * Mapuje listę elementów koszyka.
     */
    List<CartItemDto> toItemDtoList(List<CartItem> cartItems);
}
