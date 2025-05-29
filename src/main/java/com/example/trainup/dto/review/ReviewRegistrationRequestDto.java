package com.example.trainup.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRegistrationRequestDto(
        @NotNull(message = "The rating can not be blank")
        @Min(value = 1, message = "The rating must be at least 1")
        @Max(value = 5, message = "The rating must be no more than 5")
        int rating,

        @Size(min = 10, message = "The description must be at least 10 characters")
        String description
) {
}
