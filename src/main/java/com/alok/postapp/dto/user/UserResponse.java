package com.alok.postapp.dto.user;

import com.alok.postapp.enums.Permission;
import com.alok.postapp.enums.Role;

import java.time.Instant;
import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        Role role,
        Set<Permission> permissions,
        Instant createdAt,
        Instant updatedAt
) {
}
