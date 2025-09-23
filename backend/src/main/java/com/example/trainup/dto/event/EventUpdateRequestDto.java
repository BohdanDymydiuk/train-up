package com.example.trainup.dto.event;

import com.example.trainup.dto.AddressDto;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        String name,
        Long sportId,
        String description,

        @FutureOrPresent(message = "Event dateTime cannot be in the past")
        LocalDateTime dateTime,

        Boolean onlineTraining,
        Integer intensity,
        AddressDto location,
        Integer durationInMin
) {
}
