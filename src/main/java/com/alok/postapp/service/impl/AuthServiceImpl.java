package com.alok.postapp.service.impl;

import com.alok.postapp.dto.auth.LoginRequest;
import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.mapper.UserMapper;
import com.alok.postapp.service.AuthService;

public class AuthServiceImpl implements AuthService {
    UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserResponse signup(SignupRequest signupRequest) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return null;
    }
}
