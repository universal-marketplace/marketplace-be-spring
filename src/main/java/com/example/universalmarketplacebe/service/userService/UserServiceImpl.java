package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.listingResponse.ListingDto;
import com.example.universalmarketplacebe.dto.reviewResponse.ReviewDto;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
}
