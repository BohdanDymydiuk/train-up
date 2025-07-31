package com.example.trainup.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class ValidPhoneNumbersValidator
        implements ConstraintValidator<ValidPhoneNumbers, Set<String>> {
    private String regexp;

    @Override
    public void initialize(ValidPhoneNumbers constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(Set<String> phoneNumbers,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (phoneNumbers == null || phoneNumbers.isEmpty()) {
            return true;
        }
        for (String phoneNumber : phoneNumbers) {
            if (phoneNumber == null || !phoneNumber.matches(regexp)) {
                return false;
            }
        }
        return true;
    }
}
