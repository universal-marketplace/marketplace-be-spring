package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.response.OrderItemDto;
import com.example.universalmarketplacebe.dto.response.OrderResponse;
import com.example.universalmarketplacebe.model.Order;
import com.example.universalmarketplacebe.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponse toDto(Order order);
    List<OrderResponse> toDtoList(List<Order> orders);

    // Map individual order items
    @Mapping(target = "listingId", source = "listing.id")
    @Mapping(target = "title", source = "listing.title")
    @Mapping(target = "type", source = "listing.type")
    OrderItemDto toItemDto(OrderItem orderItem);
}
