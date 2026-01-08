package com.alok.postapp.service;

import com.alok.postapp.dto.auth.*;
import com.alok.postapp.dto.user.UserResponse;


public interface AuthService {
    UserResponse signup(SignupRequest signupRequest);

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refreshAccessToken(String refreshToken);

    UserActivateResponse sendActivationMail(UserActivateRequest userActivateRequest);

    UserActivateSuccessResponse activateUser(String activateToken);
}
