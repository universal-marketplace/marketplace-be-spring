package com.example.universalmarketplacebe.repository.reviewRepository;

import com.example.universalmarketplacebe.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByTargetUserId(Long targetUserId);
}
