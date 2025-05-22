package com.example.trainup.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record EventRegistrationRequestDto(
        @NotBlank
        String name,

        @NotNull
        Long sportId,

        String description,

        LocalDateTime dateTime
) {
}
