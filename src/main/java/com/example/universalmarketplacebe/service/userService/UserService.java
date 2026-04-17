package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.RegisterRequest;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    UserDto getUser();

    UserDto getUser(Long id);

    UserDto updateUser(UserUpdateRequest user);

    List<ListingDto> getUserListings(Long userId);

    List<ReviewDto> getUserReviews(Long userId);

    UserDto register(RegisterRequest registerRequest);
}
