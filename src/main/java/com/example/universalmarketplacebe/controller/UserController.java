package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public User getUser() {
        return userService.getUser();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/me")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}/listings")
    public User getUserListings(@PathVariable Long userId) {
        return userService.getUserListings(userId);
    }

    @GetMapping("/{userId}/reviews")
    public User getUserReviews(@PathVariable Long userId) {
        return userService.getUserReviews(userId);
    }


}
