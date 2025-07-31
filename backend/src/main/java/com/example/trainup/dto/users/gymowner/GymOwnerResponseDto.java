package com.example.trainup.dto.users.gymowner;

import com.example.trainup.model.enums.Gender;
import java.time.LocalDate;
import java.util.Set;

public record GymOwnerResponseDto(
        Long id,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dateOfBirth,
        String profileImageUrl,
        String email,
        String userType,
        Set<String> phoneNumbers,
        Set<Long> ownedGymIds
) {
}
