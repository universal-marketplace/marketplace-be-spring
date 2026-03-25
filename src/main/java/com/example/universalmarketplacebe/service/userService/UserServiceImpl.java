package com.example.universalmarketplacebe.service.userService;

import com.example.universalmarketplacebe.dto.userRequest.UserUpdateRequest;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.mapper.UserMapper;
import com.example.universalmarketplacebe.model.User;
import com.example.universalmarketplacebe.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto getUser() {
        return null;
    }

    @Override
    public UserDto getUser(Long id) {
        return null;
    }

    @Override
    public UserDto updateUser(UserUpdateRequest user) {
        return null;
    }

    @Override
    public UserDto getUserListings(Long userId) {
        return null;
    }

    @Override
    public UserDto getUserReviews(Long userId) {
        return null;
    }
}
