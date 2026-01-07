package com.alok.postapp.annotations;

import com.alok.postapp.enums.Role;
import com.alok.postapp.validators.AllowedRolesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static  java.lang.annotation.ElementType.*;
import static  java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({
        FIELD,
        PARAMETER,
        CONSTRUCTOR
})
@Retention(RUNTIME)
@Constraint(validatedBy = AllowedRolesValidator.class)
public @interface AllowedRoles {
    Role[] value() default {};
//    Role[] roles() default {};  // if you write "roles", then while calling the annotation user have to mention "role" explicitly

    String message() default "This role cannot be selected by the user.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
