package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(String email, UserUpdateRequest user) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (user.email() != null && !user.email().equals(email)) {
            if (userRepository.findByEmail(user.email()).isPresent()) {
                throw new IllegalArgumentException("User with this email already exists");
            }
            if (user.emailRepeated() == null || !user.email().equals(user.emailRepeated())) {
                throw new IllegalArgumentException("Emails do not match");
            }
        }
        
        userMapper.updateEntityFromRequest(user, existingUser);
        User updatedUser = userRepository.save(existingUser);

        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return userMapper.toDto(updatedUser);
    }

    @Override
    public PageResponse<ListingDto> getUserListings(Long userId, Pageable pageable) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        Page<Listing> listingPage = listingRepository.findAllByAdvertiserId(userId, pageable);
        Page<ListingDto> dtoPage = listingPage.map(listingMapper::toDto);
        return new PageResponse<>(dtoPage);
    }

    @Override
    public PageResponse<ReviewDto> getUserReviews(Long userId, Pageable pageable) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        Page<Review> reviewPage = reviewRepository.findAllByTargetUserId(userId, pageable);
        Page<ReviewDto> dtoPage = reviewPage.map(reviewMapper::toDto);
        return new PageResponse<>(dtoPage);
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
