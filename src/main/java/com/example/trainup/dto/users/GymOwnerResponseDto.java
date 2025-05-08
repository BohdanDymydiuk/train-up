package com.example.trainup.dto.users;

import java.time.LocalDate;
import java.util.Set;

public record GymOwnerResponseDto(
        Long id,
        String firstName,
        String lastName,
        Character maleOrFemale,
        LocalDate dateOfBirth,
        String profileImageUrl,
        String email,
        String userType,
        Set<String> phoneNumbers,
        Set<Long> ownedGymIds
) {
}
