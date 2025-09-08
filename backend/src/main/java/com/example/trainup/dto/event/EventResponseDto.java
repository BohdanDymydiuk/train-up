package com.example.trainup.dto.event;

import com.example.trainup.dto.AddressDto;
import java.time.LocalDateTime;

public record EventResponseDto(
        Long id,
        String name,
        Long sportId,
        String description,
        LocalDateTime dateTime,
        Long gymId,
        Long trainerId,
        Boolean onlineTraining,
        Integer intensity,
        AddressDto location
) {
}
