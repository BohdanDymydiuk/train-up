package com.example.trainup.validation;

import com.example.trainup.model.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventTargetValidator implements ConstraintValidator<EventTarget, Event> {
    @Override
    public boolean isValid(Event event, ConstraintValidatorContext context) {
        return (event.getGym() != null || event.getTrainer() != null);
    }
}
