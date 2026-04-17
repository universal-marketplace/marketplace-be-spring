package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.userRequest.LoginRequest;
import com.example.universalmarketplacebe.dto.userRequest.RegisterRequest;
import com.example.universalmarketplacebe.dto.userResponse.AuthResponse;
import com.example.universalmarketplacebe.dto.userResponse.UserDto;
import com.example.universalmarketplacebe.security.AuthenticationService;
import com.example.universalmarketplacebe.service.userService.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
