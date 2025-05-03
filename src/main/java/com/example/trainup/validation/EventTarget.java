package com.example.trainup.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EventTargetValidator.class)
@Documented
public @interface EventTarget {
    String message() default "The event must be associated with either a gym or a trainer or both.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
