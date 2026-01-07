package com.alok.postapp.dto.auth;

import com.alok.postapp.annotations.AllowedRoles;
import com.alok.postapp.annotations.PasswordValidation;
import com.alok.postapp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import static com.alok.postapp.enums.Role.*;

public record SignupRequest(
        @Email @NotBlank String email,
        @PasswordValidation String password,
        @AllowedRoles({CREATOR, ADMIN}) Role role
) {
}
