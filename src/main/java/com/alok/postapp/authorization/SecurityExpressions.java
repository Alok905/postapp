package com.alok.postapp.authorization;

import com.alok.postapp.enums.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component("security")  // bean name
public class SecurityExpressions {

    public boolean hasPermission(Permission... permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated())
            return false;

        Set<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return Arrays.stream(permissions)
                .map(Enum::name)
                .anyMatch(authorities::contains);
    }

    public boolean hasUserViewPermission() {
        return hasPermission(Permission.USER_VIEW);
    }

    public boolean hasUserEditPermission() {
        return hasPermission(Permission.USER_EDIT);
    }
}
