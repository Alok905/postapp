package com.alok.postapp.dto.auth;

import com.alok.postapp.annotations.AllowedRoles;
import com.alok.postapp.annotations.PasswordValidation;
import com.alok.postapp.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import static com.alok.postapp.enums.Role.*;

public record SignupRequest(
        @NotBlank @Length(min = 3, max = 40) String name,
        @Email @NotBlank String email,
        @PasswordValidation String password,
        @AllowedRoles({VIEWER, CREATOR}) Role role
) {
}
