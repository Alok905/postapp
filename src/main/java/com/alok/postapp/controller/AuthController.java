package com.alok.postapp.controller;

import com.alok.postapp.dto.auth.LoginRequest;
import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse signupResponse = authService.signup(signupRequest);

        return new ResponseEntity<>(signupResponse, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
