package com.example.universalmarketplacebe.repository.listingRepository;

import com.example.universalmarketplacebe.model.Listing;
import com.example.universalmarketplacebe.model.Type;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ListingRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private ListingRepository listingRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void connectionEstablished() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void shouldSaveAndFindListing() {
        // Given
        User advertiser = createAndSaveUser("advertiser", "advertiser@example.com");

        Listing listing = new Listing();
        listing.setTitle("Test Title");
        listing.setDescription("Test Description");
        listing.setPrice(BigDecimal.valueOf(100.0));
        listing.setImageUrl("https://example.com/image.png");
        listing.setAdvertiser(advertiser);
        listing.setType(Type.ITEM);

        // When
        Listing savedListing = listingRepository.save(listing);

        // Then
        assertThat(savedListing.getId()).isNotNull();

        Optional<Listing> foundListing = listingRepository.findById(savedListing.getId());
        assertThat(foundListing).isPresent();
    }

    @Test
    void shouldFindAllListingsByAdvertiserId() {
        // Given
        User advertiser = createAndSaveUser("advertiser2", "advertiser2@example.com");
        User anotherAdvertiser = createAndSaveUser("advertiser3", "advertiser3@example.com");

        createAndSaveListing(advertiser, "Test Title 1", 100.0, Type.ITEM);
        createAndSaveListing(advertiser, "Test Title 2", 200.0, Type.SERVICE);
        createAndSaveListing(anotherAdvertiser, "Test Title 3", 300.0, Type.ITEM);

        // When
        List<Listing> advertiserListings = listingRepository.findAllByAdvertiserId(advertiser.getId());

        // Then
        assertThat(advertiserListings).hasSize(2);
        assertThat(advertiserListings).extracting(Listing::getTitle)
                .containsExactlyInAnyOrder("Test Title 1", "Test Title 2");
    }

    @Test
    void shouldFindAllListingsByAdvertiserIdWithPagination() {
        // Given
        User advertiser = createAndSaveUser("advertiser_pag", "advertiser_pag@example.com");
        createAndSaveListing(advertiser, "Title 1", 10.0, Type.ITEM);
        createAndSaveListing(advertiser, "Title 2", 20.0, Type.ITEM);
        createAndSaveListing(advertiser, "Title 3", 30.0, Type.ITEM);

        Pageable pageable = PageRequest.of(0, 2);

        // When
        Page<Listing> page = listingRepository.findAllByAdvertiserId(advertiser.getId(), pageable);

        // Then
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    void shouldReturnEmptyListWhenAdvertiserHasNoListings() {
        // Given
        User advertiserWithoutListings = createAndSaveUser("advertiser4", "advertiser4@example.com");

        // When
        List<Listing> emptyListings = listingRepository.findAllByAdvertiserId(advertiserWithoutListings.getId());

        // Then
        assertThat(emptyListings).isEmpty();
    }

    @Test
    void shouldReturnEmptyPageWhenAdvertiserHasNoListingsWithPagination() {
        // Given
        User advertiser = createAndSaveUser("advertiser_empty_pag", "empty_pag@example.com");
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<Listing> page = listingRepository.findAllByAdvertiserId(advertiser.getId(), pageable);

        // Then
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getContent()).isEmpty();
    }

    private User createAndSaveUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword("password");
        user.setAvatarUrl("https://example.com/" + name + ".jpg");
        return userRepository.save(user);
    }

    private void createAndSaveListing(User advertiser, String title, Double price, Type type) {
        Listing listing = new Listing();
        listing.setTitle(title);
        listing.setDescription("Description");
        listing.setPrice(BigDecimal.valueOf(price));
        listing.setImageUrl("https://example.com/image.png");
        listing.setAdvertiser(advertiser);
        listing.setType(type);
        listingRepository.save(listing);
    }
}
