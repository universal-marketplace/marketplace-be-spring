package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.model.User;

public interface UserService {
    UserDto getUser();

    UserDto getUser(Long id);

    UserDto updateUser(UserUpdateRequest user);

    UserDto getUserListings(Long userId);

    UserDto getUserReviews(Long userId);
}
