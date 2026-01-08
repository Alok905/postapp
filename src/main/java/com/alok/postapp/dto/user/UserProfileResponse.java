package com.alok.postapp.dto.user;

import com.alok.postapp.enums.Permission;
import com.alok.postapp.enums.Role;

import java.time.Instant;
import java.util.Set;

/// for now UserResponse and UserProfileResponse contains same fields; if some new fields are introduced in the User then it'll be useful
public record UserProfileResponse(
        Long id,
        String email,
        String name,
        Role role,
        Set<Permission> permissions,
        Instant createdAt,
        Instant updatedAt
) {
}
