package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.response.NotificationDto;
import com.example.universalmarketplacebe.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
    List<NotificationDto> toDtoList(List<Notification> notifications);
}
