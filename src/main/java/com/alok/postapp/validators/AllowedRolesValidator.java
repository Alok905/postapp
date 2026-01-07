package com.alok.postapp.validators;

import com.alok.postapp.annotations.AllowedRoles;
import com.alok.postapp.enums.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AllowedRolesValidator implements ConstraintValidator<AllowedRoles, Role> {
    Set<Role> allowedRoles;

    @Override
    public void initialize(AllowedRoles constraint) {
        allowedRoles = Set.of(constraint.value());
    }

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        return role == null || allowedRoles.contains(role);
    }
}
