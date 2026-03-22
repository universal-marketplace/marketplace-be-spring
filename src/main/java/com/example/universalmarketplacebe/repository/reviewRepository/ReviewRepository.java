package com.example.universalmarketplacebe.repository.reviewRepository;

import com.example.universalmarketplacebe.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
}
