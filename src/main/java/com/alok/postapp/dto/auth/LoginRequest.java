package com.alok.postapp.dto.auth;

import com.alok.postapp.annotations.PasswordValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Email @NotBlank String email,
        @PasswordValidation String password
) {
}
