package com.example.universalmarketplacebe.security;

import com.example.universalmarketplacebe.dto.userRequest.LoginRequest;
import com.example.universalmarketplacebe.dto.userResponse.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email()
                        , loginRequest.password())
        );

        String token = jwtUtil.generateToken(authenticate.getName());
        return new AuthResponse(token);
    }
}
