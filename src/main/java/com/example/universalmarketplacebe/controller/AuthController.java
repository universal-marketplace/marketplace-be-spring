package com.example.universalmarketplacebe.controller;

import com.example.universalmarketplacebe.dto.request.LoginRequest;
import com.example.universalmarketplacebe.dto.request.RegisterRequest;
import com.example.universalmarketplacebe.dto.response.AuthResponse;
import com.example.universalmarketplacebe.dto.response.UserDto;
import com.example.universalmarketplacebe.exception.ErrorResponse;
import com.example.universalmarketplacebe.security.AuthenticationService;
import com.example.universalmarketplacebe.service.userService.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Endpoints for user registration and login")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data or user already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public UserDto register(@RequestBody @Valid RegisterRequest registerRequest) {
        return userService.register(registerRequest);
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token")
    @ApiResponse(responseCode = "200", description = "Successfully authenticated")
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    public AuthResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
