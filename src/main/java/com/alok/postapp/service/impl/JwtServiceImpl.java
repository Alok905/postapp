package com.alok.postapp.service.impl;

import com.alok.postapp.entity.User;
import com.alok.postapp.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("jwt.secretKey")
    private final String jwtSecret;


    @Override
    public String generateAccessToken(User user) {
        Key jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(jwtSecretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        Key jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(jwtSecretKey)
                .compact();
    }

    @Override
    public Boolean validateAccessToken(String accessToken, User user) {
        return null;
    }

    @Override
    public Boolean validateRefreshToken(String refreshToken, User user) {
        return null;
    }
}
