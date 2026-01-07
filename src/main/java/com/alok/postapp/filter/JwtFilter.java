package com.alok.postapp.filter;

import com.alok.postapp.entity.User;
import com.alok.postapp.service.JwtService;
import com.alok.postapp.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtService jwtService;

    public final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // if there is no authorization or like that, then simply move forward without updating the security context holder
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = authHeader.substring(7);

        try {
            Long userId = jwtService.getUserIdFromAccessToken(accessToken);

            if(userId == null) {
                filterChain.doFilter(request,response);
                return;
            }

            User user = userService.getUserById(userId);

            if(user == null) {
                filterChain.doFilter(request,response);
                return;
            }

            if(SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request,response);
        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, ex);
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
        filterChain.doFilter(request,response);
    }
}
