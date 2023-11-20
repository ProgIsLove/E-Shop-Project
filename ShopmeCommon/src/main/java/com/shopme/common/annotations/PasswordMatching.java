package com.shopme.common.annotations;

import com.shopme.common.validation.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatching {

    String message() default "Passwords don't match!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
