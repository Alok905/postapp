package com.alok.postapp.service;

import com.alok.postapp.dto.auth.LoginRequest;
import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;


public interface AuthService {
    UserResponse signup(SignupRequest signupRequest);

    LoginResponse login(LoginRequest loginRequest);
}
