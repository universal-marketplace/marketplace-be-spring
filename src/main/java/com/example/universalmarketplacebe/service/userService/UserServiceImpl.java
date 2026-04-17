package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
import com.example.universalmarketplacebe.dto.userRequest.RegisterRequest;
import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.mapper.ListingMapper;
import com.example.universalmarketplacebe.mapper.ReviewMapper;
import com.example.universalmarketplacebe.mapper.UserMapper;
import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.Review;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.listingRepository.ListingRepository;
import com.example.universalmarketplacebe.repository.reviewRepository.ReviewRepository;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;
    private final ReviewRepository reviewRepository;
    private final UserMapper userMapper;
    private final ListingMapper listingMapper;
    private final ReviewMapper reviewMapper;


    @Override
    public UserDto getUser() {
        return null;
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(UserUpdateRequest user) {
        return null;
    }

    @Override
    public List<ListingDto> getUserListings(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        List<Listing> allByAdvertiserId = listingRepository.findAllByAdvertiserId(userId);
        return listingMapper.toDtoList(allByAdvertiserId);
    }

    @Override
    public List<ReviewDto> getUserReviews(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        List<Review> allByTargetUserId = reviewRepository.findAllByTargetUserId(userId);
        return reviewMapper.toDtoList(allByTargetUserId);
    }

    @Override
    public UserDto register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }
        if (!registerRequest.password().equals(registerRequest.passwordRepeated())) {
            throw new IllegalArgumentException("Passwords do not match");
        }
        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }
}
