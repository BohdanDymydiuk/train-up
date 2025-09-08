package com.example.trainup.dto.event;

import com.example.trainup.dto.AddressDto;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.hibernate.validator.constraints.Range;

public record EventRegistrationRequestDto(
        @NotBlank(message = "Name must not be blank")
        String name,

        @NotNull(message = "SportId must not be null")
        Long sportId,

        String description,

        @NotNull(message = "Event dateTime cannot be null")
        @FutureOrPresent(message = "Event dateTime cannot be in the past")
        LocalDateTime dateTime,

        Boolean onlineTraining,

        @Range(min = 1, max = 3, message = "Intensity must be in the range of 1 to 3")
        @NotNull(message = "Intensity must not be null")
        Integer intensity,

        @NotNull(message = "Event's location can not be null")
        AddressDto location
) {
}
