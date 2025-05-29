package com.example.trainup.dto.users.trainer;

import com.example.trainup.model.enums.Gender;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record TrainerUpdateRequestDto(
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dateOfBirth,
        String profileImageUrl,
        Set<String> phoneNumbers,
        Set<Long> sportIds,
        Set<Long> gymIds,
        TrainerAddressDto location,
        Boolean onlineTraining,
        List<String> certificates,
        String description,
        String socialMediaLinks
) {
}
