package com.alok.postapp.controller;

import com.alok.postapp.dto.auth.LoginRequest;
import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.auth.SignupRequest;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import com.alok.postapp.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;

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
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(loginRequest);

        // add the refreshToken in the cookie
        String refreshToken = loginResponse.refreshToken();

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<LoginResponse> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> c.getName().equals("refresh_token"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationServiceException("Refresh token not found inside the Cookies."));

        LoginResponse loginResponse = authService.refreshAccessToken(refreshToken);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }
}
