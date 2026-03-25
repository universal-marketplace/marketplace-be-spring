package com.example.universalmarketplacebe.repository.userRepository;

import com.example.universalmarketplacebe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
