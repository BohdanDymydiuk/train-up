package com.example.trainup.dto.event;

import java.time.LocalDate;

public record EventFilterRequestDto(
        Long id,
        String name,
        Long sportId,
        LocalDate date,
        Long gymId,
        Long trainerId
) {
}
