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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    // Tests for getUser() (Current logged-in user)
    // ==========================================

    @Test
    @DisplayName("getUser() - Happy Path: Powinien zwrócić aktualnie zalogowanego użytkownika")
    void getUser_HappyPath_ReturnsCurrentUser() {
        // Given
        UserDto expectedDto = createMockUserDto();
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        // When
        UserDto result = userService.getUser();

        // Then
        assertNotNull(result, "Wynik nie powinien być nullem");
        assertEquals(expectedDto, result, "Zwrócony UserDto powinien zgadzać się z zmockowanym");
    }

    @Test
    @DisplayName("getUser() - Worse Path: Powinien rzucić wyjątek gdy brak zalogowanego użytkownika")
    void getUser_WorsePath_ThrowsExceptionWhenNotAuthenticated() {
        assertThrows(RuntimeException.class, () -> userService.getUser(),
                "Powinien rzucić wyjątek gdy mechanizm pobierania aktualnego użytkownika zawiedzie");
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
        assertThrows(RuntimeException.class, () -> userService.getUser(userId),
                "Powinien rzucić wyjątek gdy użytkownik o danym ID nie został znaleziony");
        
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUser(Long) - Edge Case: Powinien rzucić wyjątek (IllegalArgumentException) gdy ID to null")
    void getUserById_EdgeCase_ThrowsExceptionWhenIdIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(null),
                "Powinien rzucić wyjątek IllegalArgumentException, gdy przekazane ID to null");
    }

    @Test
    @DisplayName("getUser(Long) - Edge Case: Powinien rzucić wyjątek (IllegalArgumentException) gdy ID jest ujemne lub zerowe")
    void getUserById_EdgeCase_ThrowsExceptionWhenIdIsNegativeOrZero() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(-1L));
        assertThrows(IllegalArgumentException.class, () -> userService.getUser(0L));
    }

    // ==========================================
    // Tests for updateUser(UserUpdateRequest request)
    // ==========================================

    @Test
    @DisplayName("updateUser() - Happy Path: Powinien zaktualizować i zwrócić zaktualizowanego użytkownika")
    void updateUser_HappyPath_UpdatesAndReturnsUser() {
        // Given
        UserUpdateRequest request = createMockUpdateRequest();
        User updatedUser = new User();
        UserDto expectedDto = createMockUserDto();

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);

        // When
        UserDto result = userService.updateUser(request);

        // Then
        assertNotNull(result, "Wynik nie powinien być nullem");
        assertEquals(expectedDto, result, "Powinien zwrócić DTO zaktualizowanego użytkownika");
    }

    @Test
    @DisplayName("updateUser() - Worse Path: Powinien rzucić wyjątek, jeśli użytkownik nie istnieje")
    void updateUser_WorsePath_ThrowsExceptionWhenUserNotFound() {
        // Given
        UserUpdateRequest request = createMockUpdateRequest();

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(request));
    }

    @Test
    @DisplayName("updateUser() - Edge Case: Powinien rzucić IllegalArgumentException gdy podany Request to null")
    void updateUser_EdgeCase_ThrowsExceptionWhenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(null));
    }

    // ==========================================
    // Tests for getUserListings(Long userId)
    // ==========================================

    @Test
    @DisplayName("getUserListings(Long) - Happy Path: Powinien zwrócić listę ogłoszeń przypisanych do użytkownika")
    void getUserListings_HappyPath_ReturnsListings() {
        // Given
        Long userId = 1L;
        Listing listing = new Listing();
        List<Listing> listings = List.of(listing);
        ListingDto listingDto = new ListingDto(1L, "Title", "Desc", "10.00 PLN", "url", userId, "Name", "Avatar", 5.0, 1, List.of("tag"), "TYPE");
        List<ListingDto> expectedDtos = List.of(listingDto);

        when(listingRepository.findAllByAdvertiserId(userId)).thenReturn(listings);
        when(listingMapper.toDtoList(listings)).thenReturn(expectedDtos);

        // When
        List<ListingDto> result = userService.getUserListings(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDtos, result);
        verify(listingRepository).findAllByAdvertiserId(userId);
    }

    @Test
    @DisplayName("getUserListings(Long) - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null lub <= 0")
    void getUserListings_EdgeCase_ThrowsExceptionWhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(null));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(0L));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserListings(-1L));
    }

    // ==========================================
    // Tests for getUserReviews(Long userId)
    // ==========================================

    @Test
    @DisplayName("getUserReviews(Long) - Happy Path: Powinien zwrócić listę opinii o użytkowniku")
    void getUserReviews_HappyPath_ReturnsReviews() {
        // Given
        Long userId = 1L;
        Review review = new Review();
        List<Review> reviews = List.of(review);
        ReviewDto reviewDto = new ReviewDto(1L, 2L, "Author", "Avatar", userId, 5, "Great", LocalDateTime.now(), "Thanks", LocalDateTime.now());
        List<ReviewDto> expectedDtos = List.of(reviewDto);

        when(reviewRepository.findAllByTargetUserId(userId)).thenReturn(reviews);
        when(reviewMapper.toDtoList(reviews)).thenReturn(expectedDtos);

        // When
        List<ReviewDto> result = userService.getUserReviews(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedDtos, result);
        verify(reviewRepository).findAllByTargetUserId(userId);
    }

    @Test
    @DisplayName("getUserReviews(Long) - Edge Case: Powinien rzucić IllegalArgumentException dla ID równego null lub <= 0")
    void getUserReviews_EdgeCase_ThrowsExceptionWhenIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(null));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(0L));
        assertThrows(IllegalArgumentException.class, () -> userService.getUserReviews(-1L));
    }
}
