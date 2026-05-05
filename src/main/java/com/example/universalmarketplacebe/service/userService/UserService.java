package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.response.PageResponse;
import com.example.universalmarketplacebe.dto.response.ListingDto;
import com.example.universalmarketplacebe.dto.response.ReviewDto;
import com.example.universalmarketplacebe.dto.request.RegisterRequest;
import com.example.universalmarketplacebe.dto.request.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.response.UserDto;
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
