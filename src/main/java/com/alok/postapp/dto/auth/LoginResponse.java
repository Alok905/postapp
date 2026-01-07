package com.alok.postapp.dto.auth;

import com.alok.postapp.dto.user.UserResponse;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}
