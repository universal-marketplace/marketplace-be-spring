package com.example.universalmarketplacebe.repository.reviewRepository;

import com.example.universalmarketplacebe.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByTargetUserId(Long targetUserId, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.targetUser.id = :userId")
    Double getAverageRatingForUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.targetUser.id = :userId")
    Long getReviewCountForUser(@Param("userId") Long userId);
}
