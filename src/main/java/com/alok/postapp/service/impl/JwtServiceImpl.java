package com.alok.postapp.service.impl;

import com.alok.postapp.entity.Session;
import com.alok.postapp.entity.User;
import com.alok.postapp.exception.ResourceNotFoundException;
import com.alok.postapp.repository.SessionRepository;
import com.alok.postapp.repository.UserRepository;
import com.alok.postapp.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("role", user.getRole())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getKey())
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(getKey())
                .compact();
    }

    @Override
    public Claims getClaimsFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    public Long getUserIdFromAccessToken(String token) {
        return  Long.parseLong(getClaimsFromToken(token).getSubject());
    }

    public Long getUserIdFromRefreshToken(String token) {
        return  Long.parseLong(getClaimsFromToken(token).getSubject());
    }

    @Override
    public Boolean validateAccessToken(String accessToken, User user) {
        Claims claims = getClaimsFromToken(accessToken);

        return claims.getSubject().equals(user.getId().toString());
    }

    @Override
    public Boolean validateRefreshToken(String refreshToken, User user) {
        return true;
    }

}
