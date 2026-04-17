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
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        User advertiser = new User();
        advertiser.setName("advertiser");
        advertiser.setEmail("advertiser@example.com");
        advertiser.setPassword("password");
        advertiser.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(advertiser);

        Listing listing = new Listing();
        listing.setTitle("Test Title");
        listing.setDescription("Test Description");
        listing.setPrice(BigDecimal.valueOf(100.0));
        listing.setImageUrl("http://example.com/image.png");
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
        User advertiser = new User();
        advertiser.setName("advertiser2");
        advertiser.setEmail("advertiser2@example.com");
        advertiser.setPassword("password");
        advertiser.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(advertiser);

        User anotherAdvertiser = new User();
        anotherAdvertiser.setName("advertiser3");
        anotherAdvertiser.setEmail("advertiser3@example.com");
        anotherAdvertiser.setPassword("password");
        anotherAdvertiser.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(anotherAdvertiser);

        Listing listing1 = new Listing();
        listing1.setTitle("Test Title 1");
        listing1.setDescription("Test Description 1");
        listing1.setPrice(BigDecimal.valueOf(100.0));
        listing1.setImageUrl("http://example.com/image1.png");
        listing1.setAdvertiser(advertiser);
        listing1.setType(Type.ITEM);
        listingRepository.save(listing1);

        Listing listing2 = new Listing();
        listing2.setTitle("Test Title 2");
        listing2.setDescription("Test Description 2");
        listing2.setPrice(BigDecimal.valueOf(200.0));
        listing2.setImageUrl("http://example.com/image2.png");
        listing2.setAdvertiser(advertiser);
        listing2.setType(Type.SERVICE);
        listingRepository.save(listing2);

        Listing listing3 = new Listing();
        listing3.setTitle("Test Title 3");
        listing3.setDescription("Test Description 3");
        listing3.setPrice(BigDecimal.valueOf(300.0));
        listing3.setImageUrl("http://example.com/image3.png");
        listing3.setAdvertiser(anotherAdvertiser);
        listing3.setType(Type.ITEM);
        listingRepository.save(listing3);

        // When
        List<Listing> advertiserListings = listingRepository.findAllByAdvertiserId(advertiser.getId());

        // Then
        assertThat(advertiserListings).hasSize(2);
        assertThat(advertiserListings).extracting(Listing::getTitle)
                .containsExactlyInAnyOrder("Test Title 1", "Test Title 2");
    }

    @Test
    void shouldReturnEmptyListWhenAdvertiserHasNoListings() {
        // Given
        User advertiserWithoutListings = new User();
        advertiserWithoutListings.setName("advertiser4");
        advertiserWithoutListings.setEmail("advertiser4@example.com");
        advertiserWithoutListings.setPassword("password");
        advertiserWithoutListings.setAvatarUrl("http://example.com/avatar.png");
        userRepository.save(advertiserWithoutListings);

        // When
        List<Listing> emptyListings = listingRepository.findAllByAdvertiserId(advertiserWithoutListings.getId());

        // Then
        assertThat(emptyListings).isEmpty();
    }
}