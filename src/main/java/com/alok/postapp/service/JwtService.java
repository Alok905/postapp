package com.alok.postapp.service;

import com.alok.postapp.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Boolean validateAccessToken(String accessToken, User user);

    Boolean validateRefreshToken(String refreshToken, User user);
}
