package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserDto getUser() {
        return userService.getUser();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/me")
    public UserDto updateUser(@RequestBody UserUpdateRequest user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}/listings")
    public List<ListingDto> getUserListings(@PathVariable Long userId) {
        return userService.getUserListings(userId);
    }

    @GetMapping("/{userId}/reviews")
    public List<ReviewDto> getUserReviews(@PathVariable Long userId) {
        return userService.getUserReviews(userId);
    }


}
