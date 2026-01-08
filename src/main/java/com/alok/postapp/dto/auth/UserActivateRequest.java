package com.alok.postapp.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserActivateRequest(
        @NotBlank @Email String email
) {
}
