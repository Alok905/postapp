package com.alok.postapp.service;

import com.alok.postapp.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    String generateActivateToken(User user);

    Claims getClaimsFromToken(String refreshToken);

    Long getUserIdFromAccessToken(String token);

    Long getUserIdFromRefreshToken(String token);

    Long getUserIdFromActivateToken(String token);

}
