package com.example.trainup.dto.event;

import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        String name,
        Long sportId,
        String description,

        @FutureOrPresent(message = "Event dateTime cannot be in the past")
        LocalDateTime dateTime
) {
}
