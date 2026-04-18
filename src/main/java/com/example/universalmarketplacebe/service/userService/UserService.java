package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.PageResponse;
import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.RegisterRequest;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    UserDto getUser(String email);

    UserDto getUser(Long id);

    UserDto updateUser(String email, UserUpdateRequest user);

    PageResponse<ListingDto> getUserListings(Long userId, Pageable pageable);

    PageResponse<ReviewDto> getUserReviews(Long userId, Pageable pageable);

    UserDto register(RegisterRequest registerRequest);
}
