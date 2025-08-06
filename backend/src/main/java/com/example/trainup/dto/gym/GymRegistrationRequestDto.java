package com.example.trainup.dto.gym;

import com.example.trainup.model.WorkingHoursEntry;
import com.example.trainup.validation.ValidPhoneNumbers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record GymRegistrationRequestDto(
        @NotBlank(message = "Name can not be blank")
        String name,

        @NotNull(message = "Gym's location can not be null")
        GymAddressDto location,

        @NotEmpty(message = "Sport Ids can not be empty")
        Set<Long> sportIds,

        String description,

        String website,

        @NotEmpty(message = "Phone number can not be empty")
        @ValidPhoneNumbers
        Set<String> phoneNumbers,

        Set<WorkingHoursEntry> workingHours,

        Set<Long> trainerIds,

        @Size(max = 5, message = "The maximum number of gym photos is 5.")
        Set<String> photoUrls
) {
}
