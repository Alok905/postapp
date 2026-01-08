package com.alok.postapp.dto.user;

import com.alok.postapp.enums.Permission;
import com.alok.postapp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record UserProfileUpdateRequest(
        @Email String email,
        @Length(min = 3, max = 40) String name,
        Role role,
        Set<Permission> permissions
) {
}
