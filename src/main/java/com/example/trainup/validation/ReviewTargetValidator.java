package com.example.trainup.validation;

import com.example.trainup.model.Review;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ReviewTargetValidator implements ConstraintValidator<ReviewTarget, Review> {
    @Override
    public boolean isValid(Review review, ConstraintValidatorContext context) {
        return (review.getGym() != null && review.getTrainer() == null)
                || (review.getGym() == null && review.getTrainer() != null);
    }
}
