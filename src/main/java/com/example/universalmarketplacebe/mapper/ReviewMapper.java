package com.example.universalmarketplacebe.mapper;

import com.example.universalmarketplacebe.configuration.MapperConfig;
import com.example.universalmarketplacebe.dto.reviewRequest.ReviewCreateRequest;
import com.example.universalmarketplacebe.dto.reviewResponse.ReplyDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.model.Reply;
import com.example.universalmarketplacebe.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {

    /**
     * Mapuje encję Review na ReviewDto z pełną listą odpowiedzi.
     */
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "authorAvatar", source = "author.avatarUrl")
    @Mapping(target = "targetId", source = "targetUser.id")
    @Mapping(target = "date", source = "createdAt")
    @Mapping(target = "replies", source = "replies", qualifiedByName = "mapOnlyRootReplies")
    ReviewDto toDto(Review review);

    /**
     * Mapuje encję Reply na ReplyDto (rekurencyjnie dla dzieci).
     */
    @Mapping(target = "authorId", source = "user.id")
    @Mapping(target = "authorName", source = "user.name")
    @Mapping(target = "authorAvatar", source = "user.avatarUrl")
    @Mapping(target = "replies", source = "childReplies")
    ReplyDto toReplyDto(Reply reply);

    List<ReplyDto> toReplyDtoList(List<Reply> replies);

    List<ReviewDto> toDtoList(List<Review> reviews);

    /**
     * Pomocnicza metoda, która filtruje tylko główne odpowiedzi (te bez rodzica), 
     * aby nie dublować zagnieżdżonych odpowiedzi na głównej liście.
     */
    @Named("mapOnlyRootReplies")
    default List<ReplyDto> mapOnlyRootReplies(List<Reply> replies) {
        if (replies == null) return null;
        return replies.stream()
                .filter(r -> r.getParentReply() == null)
                .map(this::toReplyDto)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "targetUser", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Review toEntity(ReviewCreateRequest request);
}
