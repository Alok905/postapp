package com.alok.postapp.validators;

import com.alok.postapp.annotations.PasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

///        if you want to add custom message rather than keeping that default one then context is used
//        if(password == null) {
//            context.disableDefaultConstraintViolation();
//
//            context
//                    .buildConstraintViolationWithTemplate("Password cannot be null.")
//                    .addConstraintViolation();
//
//            return false;
//        }
        if(context == null) return true;  /// because null check should be done by @NotBlank or @NotNull, its not the headache of this validator

        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

        return password.matches(regex);
    }
}
