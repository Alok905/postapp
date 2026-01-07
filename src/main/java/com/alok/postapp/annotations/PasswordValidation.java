package com.alok.postapp.annotations;

import com.alok.postapp.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static  java.lang.annotation.ElementType.*;
import static  java.lang.annotation.RetentionPolicy.*;

@Target({
        FIELD,
        PARAMETER,
        CONSTRUCTOR
})
@Retention(value = RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordValidation {
    String message() default "Password should contain at least 4 characters. containing at least one uppercase, lowercase, number, special character.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
