package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.model.User;

public interface UserService {
    User getUser();

    User getUser(Long id);

    User updateUser(User user);

    User getUserListings(Long userId);

    User getUserReviews(Long userId);
}
