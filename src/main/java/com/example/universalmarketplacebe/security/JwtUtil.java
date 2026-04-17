package com.example.universalmarketplacebe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey secret;

    @Value("${jwt.expiration.hour}")
    private Long expirationInHours;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.secret = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(System.currentTimeMillis()
                                + expirationInHours
                                * 60 * 60 * 1000))
                .signWith(secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            return !getClaimsJws(token).getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return getClaimsJws(token).getSubject();
    }

    private Claims getClaimsJws(String token) {
        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
