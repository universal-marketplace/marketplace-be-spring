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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ListingMapper listingMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto createMockUserDto() {
        return new UserDto(1L, "Test User", "test@example.com", "avatar.png", "Description", 5.0, 10);
    }

    private UserUpdateRequest createMockUpdateRequest() {
        return new UserUpdateRequest("Updated Name", "new@example.com", "new@example.com", "new_avatar.png", "New Description");
    }

    // ==========================================
    // Tests for getUser(String email)
    // ==========================================

    @Test
    @DisplayName("getUser(String) - Happy Path")
    void getUser_HappyPath() {
        String email = "test@example.com";
        User mockUser = new User();
        UserDto expectedDto = createMockUserDto();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(expectedDto);

        UserDto result = userService.getUser(email);

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("getUser(String) - Worse Path: Not Found")
    void getUser_NotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(email));
    }

    // ==========================================
    // Tests for getUser(Long id)
    // ==========================================

    @Test
    @DisplayName("getUser(Long) - Happy Path")
    void getUserById_HappyPath() {
        Long userId = 1L;
        User mockUser = new User();
        UserDto mockDto = createMockUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockDto);

        UserDto result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(mockDto, result);
    }

    @Test
    @DisplayName("getUser(Long) - Worse Path: Not Found")
    void getUserById_NotFound() {
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(userId));
    }

    // ==========================================
    // Tests for updateUser()
    // ==========================================

    @Test
    @DisplayName("updateUser() - Happy Path")
    void updateUser_HappyPath() {
        String email = "test@example.com";
        UserUpdateRequest request = createMockUpdateRequest();
        User existingUser = new User();
        UserDto expectedDto = createMockUserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        UserDto result = userService.updateUser(email, request);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("updateUser() - Worse Path: User Not Found")
    void updateUser_UserNotFound() {
        String email = "test@example.com";
        UserUpdateRequest request = createMockUpdateRequest();
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(email, request));
    }

    @Test
    @DisplayName("updateUser() - Worse Path: Email Already Exists")
    void updateUser_EmailAlreadyExists() {
        String email = "test@example.com";
        UserUpdateRequest request = createMockUpdateRequest();
        User existingUser = new User();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(email, request));
    }

    @Test
    @DisplayName("updateUser() - Worse Path: Emails Do Not Match")
    void updateUser_EmailsDoNotMatch() {
        String email = "test@example.com";
        UserUpdateRequest request = new UserUpdateRequest("Name", "new@example.com", "wrong@example.com", null, null);
        User existingUser = new User();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(email, request));
    }

    // ==========================================
    // Tests for getUserListings()
    // ==========================================

    @Test
    @DisplayName("getUserListings() - Happy Path")
    void getUserListings_HappyPath() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Listing> listingPage = new PageImpl<>(List.of(new Listing()));
        
        when(listingRepository.findAllByAdvertiserId(userId, pageable)).thenReturn(listingPage);
        when(listingMapper.toDto(any())).thenReturn(mock(ListingDto.class));

        PageResponse<ListingDto> result = userService.getUserListings(userId, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }

    @Test
    @DisplayName("getUserListings() - Worse Path: Invalid ID")
    void getUserListings_InvalidId() {
        Pageable pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(null, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(0L, pageable));
    }

    // ==========================================
    // Tests for getUserReviews()
    // ==========================================

    @Test
    @DisplayName("getUserReviews() - Happy Path")
    void getUserReviews_HappyPath() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviewPage = new PageImpl<>(List.of(new Review()));

        when(reviewRepository.findAllByTargetUserId(userId, pageable)).thenReturn(reviewPage);
        when(reviewMapper.toDto(any())).thenReturn(mock(ReviewDto.class));

        PageResponse<ReviewDto> result = userService.getUserReviews(userId, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
    }

    @Test
    @DisplayName("getUserReviews() - Worse Path: Invalid ID")
    void getUserReviews_InvalidId() {
        Pageable pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(null, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(0L, pageable));
    }

    // ==========================================
    // Tests for register()
    // ==========================================

    @Test
    @DisplayName("register() - Happy Path")
    void register_HappyPath() {
        RegisterRequest request = new RegisterRequest("Name", "test@example.com", "password", "password");
        User user = new User();
        UserDto dto = createMockUserDto();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.password())).thenReturn("encoded");
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.register(request);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("register() - Worse Path: Email Already Exists")
    void register_EmailExists() {
        RegisterRequest request = new RegisterRequest("Name", "test@example.com", "password", "password");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }

    @Test
    @DisplayName("register() - Worse Path: Passwords Mismatch")
    void register_PasswordMismatch() {
        RegisterRequest request = new RegisterRequest("Name", "test@example.com", "password", "wrong");
        assertThrows(IllegalArgumentException.class, () -> userService.register(request));
    }
}
