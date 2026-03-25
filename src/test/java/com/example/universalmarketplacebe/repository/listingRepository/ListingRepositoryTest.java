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

}