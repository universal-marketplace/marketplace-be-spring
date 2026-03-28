package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.listingRequest.ListingRequest;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.model.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ListingMapper {

    /**
     * Mapuje encję Listing na ListingDto (widok ogłoszenia).
     */
    @Mapping(target = "advertiserId", source = "advertiser.id")
    @Mapping(target = "advertiserName", source = "advertiser.name")
    @Mapping(target = "advertiserAvatar", source = "advertiser.avatarUrl")
    @Mapping(target = "rating", source = "advertiser.rating")
    @Mapping(target = "reviewCount", source = "advertiser.reviewCount")
    @Mapping(target = "price", source = ".", qualifiedByName = "formatPrice")
    ListingDto toDto(Listing listing);

    /**
     * Mapuje listę encji Listing na listę ListingDto.
     */
    List<ListingDto> toDtoList(List<Listing> listings);

    /**
     * Mapuje ListingRequest na nową encję Listing.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "advertiser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "price", source = "priceAmount")
    Listing toEntity(ListingRequest request);

    /**
     * Aktualizuje istniejącą encję Listing na podstawie danych z requestu.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "advertiser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "price", source = "priceAmount")
    void updateEntityFromRequest(ListingRequest request, @MappingTarget Listing listing);

    /**
     * Formatuje cenę na czytelny format String, np. "100.00 PLN"
     */
    @Named("formatPrice")
    default String formatPrice(Listing listing) {
        if (listing == null || listing.getPrice() == null) {
            return "0.00";
        }
        String currency = listing.getCurrency() != null ? listing.getCurrency() : "PLN";
        return String.format("%.2f %s", listing.getPrice().doubleValue(), currency);
    }
}
