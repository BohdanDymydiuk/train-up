package com.example.trainup.dto.users.athlete;

import java.time.LocalDate;
import java.util.Set;

public record AthleteResponseDto(
        Long id,
        String firstName,
        String lastName,
        Character maleOrFemale,
        LocalDate dateOfBirth,
        String profileImageUrl,
        String email,
        String userType,
        Set<String> phoneNumbers,
        Set<Long> sportIds,
        boolean emailPermission,
        boolean phonePermission
) {
}
