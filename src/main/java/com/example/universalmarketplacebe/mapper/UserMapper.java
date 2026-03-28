package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.userRequest.RegisterRequest;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    /**
     * Mapuje encję User na rekord UserDto.
     * Automatycznie mapuje pola o tych samych nazwach.
     */
    UserDto toDto(User user);

    /**
     * Mapuje RegisterRequest (używany przy rejestracji) na encję User.
     * Ignorujemy pola techniczne, które są ustawiane automatycznie przez bazę lub serwis.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatarUrl", constant = "default_avatar.png") // Przykładowy domyślny avatar
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "reviewCount", constant = "0")
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    User toEntity(RegisterRequest request);

    /**
     * Mapuje UserUpdateRequest na nową encję User.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    User toEntity(UserUpdateRequest request);

    /**
     * Aktualizuje istniejącą encję User na podstawie danych z UserUpdateRequest.
     * Używane w metodach typu "update", gdzie nie chcemy tworzyć nowego obiektu, a jedynie zmienić pola istniejącego.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
