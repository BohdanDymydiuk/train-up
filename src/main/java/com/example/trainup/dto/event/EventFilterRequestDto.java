package com.example.trainup.dto.event;

import java.time.LocalDateTime;

public record EventFilterRequestDto(
        Long id,
        String name,
        Long sportId,
        LocalDateTime dateTime,
        Long gymId,
        Long trainerId
) {
}
