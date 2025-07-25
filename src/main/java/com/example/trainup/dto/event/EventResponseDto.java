package com.example.trainup.dto.event;

import java.time.LocalDateTime;

public record EventResponseDto(
        Long id,
        String name,
        Long sportId,
        String description,
        LocalDateTime dateTime,
        Long gymId,
        Long trainerId
) {
}
