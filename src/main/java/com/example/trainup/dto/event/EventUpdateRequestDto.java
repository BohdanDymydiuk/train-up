package com.example.trainup.dto.event;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        String name,
        Long sportId,
        String description,
        LocalDateTime dateTime
) {
}
