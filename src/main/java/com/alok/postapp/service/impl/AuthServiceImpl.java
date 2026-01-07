package com.alok.postapp.service.impl;

import com.alok.postapp.dto.auth.LoginRequest;
import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import com.alok.postapp.enums.Role;
import com.alok.postapp.exception.ResourceNotFoundException;
import com.alok.postapp.mapper.UserMapper;
import com.alok.postapp.repository.UserRepository;
import com.alok.postapp.service.AuthService;
import com.alok.postapp.service.JwtService;
import com.alok.postapp.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Override
    public UserResponse signup(SignupRequest signupRequest) {
        User user = userMapper.signUpRequstToUser(signupRequest);

        // set the role if not present: default is VIEWER
        Role role = user.getRole();
        if(role == null)    user.setRole(Role.VIEWER);

        // encode the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.email();
        String password = loginRequest.password();

        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User", "email="+email));

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), password);

        authenticationManager.authenticate(authenticationToken);


        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user, refreshToken);

        UserResponse userResponse = userMapper.userToUserResponse(user);

        return new LoginResponse(accessToken, refreshToken, userResponse);
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromRefreshToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id="+userId));

        String accessToken = jwtService.generateAccessToken(user);

        UserResponse userResponse = userMapper.userToUserResponse(user);

        return new LoginResponse(accessToken, refreshToken, userResponse);
    }
}
