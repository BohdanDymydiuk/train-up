package com.example.trainup.dto.event;

import com.example.trainup.dto.AddressDto;
import java.time.LocalDateTime;
import java.util.Set;

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
        Set<String> photoUrls,
        AddressDto location,
        Integer durationInMin
) {
}
