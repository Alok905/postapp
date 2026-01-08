package com.alok.postapp.service.impl;

import com.alok.postapp.dto.auth.*;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import com.alok.postapp.enums.Role;
import com.alok.postapp.exception.ConflictException;
import com.alok.postapp.exception.ResourceNotFoundException;
import com.alok.postapp.mapper.UserMapper;
import com.alok.postapp.repository.UserRepository;
import com.alok.postapp.service.AuthService;
import com.alok.postapp.service.JwtService;
import com.alok.postapp.service.SessionService;
import com.alok.postapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;

    @Override
    public UserResponse signup(SignupRequest signupRequest) {
        User existingUser = userService.getUserByEmail(signupRequest.email(), false);

        if (existingUser != null) {
            throw new ConflictException("User with email: " + signupRequest.email() + " already exists");
        }

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

        User user = userService.getUserByEmail(email, true);

        if(user.getDeletedAt() != null) {
            throw new AuthenticationCredentialsNotFoundException("User has been deleted");
        }

        if(user.getDeactivatedAt() != null) {
            throw new DisabledException("User has been deactivated.");
        }

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

        User user = userService.getUserById(userId, true);

        String accessToken = jwtService.generateAccessToken(user);

        UserResponse userResponse = userMapper.userToUserResponse(user);

        return new LoginResponse(accessToken, refreshToken, userResponse);
    }

    @Override
    public UserActivateResponse sendActivationMail(UserActivateRequest userActivateRequest) {
        String email = userActivateRequest.email();

        User user = userService.getUserByEmail(email, true);
        if(user.getDeletedAt() != null) {
            throw new DisabledException("User has been deleted");
        }
        if(user.getDeactivatedAt() == null) {
            throw new ConflictException("User is already activate.");
        }

        String activateToken = jwtService.generateActivateToken(user);

        String message = "Activation link has been sent to the email: " + email + " token = " + activateToken;
        return new UserActivateResponse(
                message,
                "5 minutes"
        );
    }

    @Override
    @Transactional
    public UserActivateSuccessResponse activateUser(String activateToken) {

        Long userId = jwtService.getUserIdFromActivateToken(activateToken);

        User user = userService.getUserById(userId, true);
        if(user.getDeactivatedAt() == null) {
            throw new ConflictException("User is already activate.");
        }
        user.setDeactivatedAt(null);
        userRepository.save(user);

        return new UserActivateSuccessResponse(
                "User has been activated successfully."
        );
    }
}
