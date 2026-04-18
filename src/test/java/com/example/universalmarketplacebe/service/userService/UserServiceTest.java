package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.PageResponse;
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

import java.time.LocalDateTime;
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
    @DisplayName("getUser(String) - Happy Path: Powinien zwrócić aktualnie zalogowanego użytkownika po emailu")
    void getUser_HappyPath_ReturnsCurrentUserByEmail() {
        // Given
        String email = "test@example.com";
        User mockUser = new User();
        UserDto expectedDto = createMockUserDto();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(expectedDto);

        // When
        UserDto result = userService.getUser(email);

        // Then
        assertNotNull(result, "Wynik nie powinien być nullem");
        assertEquals(expectedDto, result, "Zwrócony UserDto powinien zgadzać się z zmockowanym");
    }

    @Test
    @DisplayName("getUser(String) - Worse Path: Powinien rzucić wyjątek gdy brak użytkownika o danym emailu")
    void getUser_WorsePath_ThrowsExceptionWhenUserNotFoundByEmail() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(email),
                "Powinien rzucić wyjątek gdy użytkownik nie zostanie znaleziony w bazie danych");
    }

    // ==========================================
    // Tests for getUser(Long id)
    // ==========================================

    @Test
    @DisplayName("getUser(Long) - Happy Path: Powinien zwrócić użytkownika gdy istnieje w bazie")
    void getUserById_HappyPath_ReturnsUserDto() {
        // Given
        Long userId = 1L;
        User mockUser = new User();
        UserDto mockDto = createMockUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userMapper.toDto(mockUser)).thenReturn(mockDto);

        // When
        UserDto result = userService.getUser(userId);

        // Then
        assertNotNull(result, "Wynik nie powinien być nullem");
        assertEquals(mockDto, result, "Zwrócony UserDto powinien zgadzać się z oczekiwanym");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUser(Long) - Worse Path: Powinien rzucić wyjątek gdy użytkownik nie istnieje")
    void getUserById_WorsePath_ThrowsExceptionWhenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(userId),
                "Powinien rzucić wyjątek gdy użytkownik o danym ID nie został znaleziony");
        
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUser(Long) - Edge Case: Powinien rzucić wyjątek (IllegalArgumentException) gdy ID to null")
    void getUserById_EdgeCase_ThrowsExceptionWhenIdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.getUser((Long)null),
                "Powinien rzucić wyjątek IllegalArgumentException, gdy przekazane ID to null");
    }

    @Test
    @DisplayName("getUser(Long) - Edge Case: Powinien rzucić wyjątek (IllegalArgumentException) gdy ID jest ujemne lub zerowe")
    void getUserById_EdgeCase_ThrowsExceptionWhenIdIsNegativeOrZero() {
        when(userRepository.findById(-1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
        
        when(userRepository.findById(0L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(0L));
    }

    // ==========================================
    // Tests for updateUser(String email, UserUpdateRequest request)
    // ==========================================

    @Test
    @DisplayName("updateUser() - Happy Path: Powinien zaktualizować i zwrócić zaktualizowanego użytkownika")
    void updateUser_HappyPath_UpdatesAndReturnsUser() {
        // Given
        String email = "test@example.com";
        UserUpdateRequest request = createMockUpdateRequest();
        User existingUser = new User();
        UserDto expectedDto = createMockUserDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDto(existingUser)).thenReturn(expectedDto);

        // When
        UserDto result = userService.updateUser(email, request);

        // Then
        assertNotNull(result, "Wynik nie powinien być nullem");
        assertEquals(expectedDto, result, "Powinien zwrócić DTO zaktualizowanego użytkownika");
        verify(userMapper).updateEntityFromRequest(request, existingUser);
    }

    @Test
    @DisplayName("updateUser() - Worse Path: Powinien rzucić wyjątek, jeśli użytkownik nie istnieje")
    void updateUser_WorsePath_ThrowsExceptionWhenUserNotFound() {
        // Given
        String email = "notfound@example.com";
        UserUpdateRequest request = createMockUpdateRequest();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(email, request));
    }

    @Test
    @DisplayName("updateUser() - Worse Path: Powinien rzucić wyjątek, jeśli nowy email jest już zajęty")
    void updateUser_WorsePath_ThrowsExceptionWhenNewEmailAlreadyExists() {
        // Given
        String email = "test@example.com";
        UserUpdateRequest request = createMockUpdateRequest();
        User existingUser = new User();
        
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(email, request));
    }

    // ==========================================
    // Tests for getUserListings(Long userId)
    // ==========================================

    @Test
    @DisplayName("getUserListings(Long, Pageable) - Happy Path: Powinien zwrócić listę ogłoszeń przypisanych do użytkownika")
    void getUserListings_HappyPath_ReturnsListings() {
        // Given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Listing listing = new Listing();
        List<Listing> listings = List.of(listing);
        Page<Listing> listingPage = new PageImpl<>(listings, pageable, 1);
        
        ListingDto listingDto = new ListingDto(1L, "Title", "Desc", "10.00 PLN", "url", userId, "Name", "Avatar", 5.0, 1, List.of("tag"), "PRODUCT");

        when(listingRepository.findAllByAdvertiserId(userId, pageable)).thenReturn(listingPage);
        when(listingMapper.toDto(any(Listing.class))).thenReturn(listingDto);

        // When
        PageResponse<ListingDto> result = userService.getUserListings(userId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(listingDto, result.content().get(0));
        assertEquals(1, result.totalElements());
        verify(listingRepository).findAllByAdvertiserId(userId, pageable);
    }

    @Test
    @DisplayName("getUserListings(Long, Pageable) - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null lub <= 0")
    void getUserListings_EdgeCase_ThrowsExceptionWhenIdIsInvalid() {
        Pageable pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(null, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(0L, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(-1L, pageable));
    }

    // ==========================================
    // Tests for getUserReviews(Long userId, Pageable pageable)
    // ==========================================

    @Test
    @DisplayName("getUserReviews(Long, Pageable) - Happy Path: Powinien zwrócić listę opinii o użytkowniku")
    void getUserReviews_HappyPath_ReturnsReviews() {
        // Given
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Review review = new Review();
        List<Review> reviews = List.of(review);
        Page<Review> reviewPage = new PageImpl<>(reviews, pageable, 1);
        ReviewDto reviewDto = new ReviewDto(1L, 2L, "Author", "Avatar", userId, 5, "Great", LocalDateTime.now(), "Thanks", LocalDateTime.now());

        when(reviewRepository.findAllByTargetUserId(userId, pageable)).thenReturn(reviewPage);
        when(reviewMapper.toDto(any(Review.class))).thenReturn(reviewDto);

        // When
        PageResponse<ReviewDto> result = userService.getUserReviews(userId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(reviewDto, result.content().get(0));
        assertEquals(1, result.totalElements());
        assertEquals(1, result.totalPages());
        verify(reviewRepository).findAllByTargetUserId(userId, pageable);
        verify(reviewMapper, times(1)).toDto(any(Review.class));
    }

    @Test
    @DisplayName("getUserReviews(Long, Pageable) - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null lub <= 0")
    void getUserReviews_EdgeCase_ThrowsExceptionWhenIdIsInvalid() {
        Pageable pageable = PageRequest.of(0, 10);
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(null, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(0L, pageable));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(-1L, pageable));
    }
}
