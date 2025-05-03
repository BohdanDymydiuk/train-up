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
@Constraint(validatedBy = ReviewTargetValidator.class)
@Documented
public @interface ReviewTarget {
    String message() default "The review must be associated with either a gym or a trainer.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
