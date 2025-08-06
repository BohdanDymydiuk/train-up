package com.example.trainup.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidPhoneNumbersValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumbers {
    String message() default "Each phone number must match the format +38(XXX)-XXX-XXXX";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String regexp() default "^\\+38\\(\\d{3}\\)\\-\\d{3}\\-\\d{4}$";
}
