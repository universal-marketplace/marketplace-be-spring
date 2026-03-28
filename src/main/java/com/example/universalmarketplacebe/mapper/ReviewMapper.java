package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {

    /**
     * Mapuje encję Review na ReviewDto.
     * Wyciąga dane autora oraz treść pierwszej odpowiedzi (jeśli istnieje).
     */
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorAvatar", source = "author.avatarUrl")
    @Mapping(target = "targetId", source = "targetUser.id")
    @Mapping(target = "date", source = "createdAt")
    @Mapping(target = "reply", source = "replies", qualifiedByName = "getLatestReplyComment")
    @Mapping(target = "replyDate", source = "replies", qualifiedByName = "getLatestReplyDate")
    ReviewDto toDto(Review review);

    /**
     * Mapuje listę encji Review na listę ReviewDto.
     */
    List<ReviewDto> toDtoList(List<Review> reviews);

    /**
     * Mapuje request utworzenia recenzji na encję Review.
     * Pola author i targetUser muszą zostać ustawione ręcznie w serwisie.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "targetUser", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Review toEntity(ReviewCreateRequest request);

    @Named("getLatestReplyComment")
    default String getLatestReplyComment(List<Reply> replies) {
        if (replies == null || replies.isEmpty()) {
            return null;
        }
        return replies.get(0).getComment();
    }

    @Named("getLatestReplyDate")
    default java.time.LocalDateTime getLatestReplyDate(List<Reply> replies) {
        if (replies == null || replies.isEmpty()) {
            return null;
        }
        return replies.get(0).getCreatedAt();
    }
}
