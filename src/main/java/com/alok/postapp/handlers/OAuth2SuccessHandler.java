package com.alok.postapp.handlers;

import com.alok.postapp.dto.auth.LoginResponse;
import com.alok.postapp.dto.user.UserResponse;
import com.alok.postapp.entity.User;
import com.alok.postapp.enums.Role;
import com.alok.postapp.mapper.UserMapper;
import com.alok.postapp.service.JwtService;
import com.alok.postapp.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        User user = userService.getUserByEmail(email, false);

        if(user == null) {
            User newUser = User.builder()
                    .name(name)
                    .email(email)
                    .role(Role.VIEWER)
                    .build();
            user = userService.saveUser(newUser);
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in production HTTPS
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);

        String frontendUrl = "http://localhost:8080/home.html?token=" + accessToken;
        response.sendRedirect(frontendUrl);

    }
}
