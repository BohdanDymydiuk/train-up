package com.example.trainup.dto.gym;

import com.example.trainup.dto.AddressDto;
import com.example.trainup.model.WorkingHoursEntry;
import com.example.trainup.validation.ValidPhoneNumbers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record GymRegistrationRequestDto(
        @NotBlank(message = "Name can not be blank")
        String name,

        @NotNull(message = "Gym's location can not be null")
        AddressDto location,

        @NotEmpty(message = "Sport Ids can not be empty")
        Set<Long> sportIds,

        String description,

        String website,

        @NotEmpty(message = "Phone number can not be empty")
        @ValidPhoneNumbers
        Set<String> phoneNumbers,

        Set<WorkingHoursEntry> workingHours,

        Set<Long> trainerIds
) {
}
