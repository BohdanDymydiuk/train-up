package com.example.trainup.dto.users.athlete;

import java.time.LocalDate;
import java.util.Set;

public record AthleteUpdateRequestDto(
        String firstName,
        String lastName,
        Character maleOrFemale,
        LocalDate dateOfBirth,
        String profileImageUrl,
        Set<String> phoneNumbers,
        Set<Long> sportIds,
        boolean emailPermission,
        boolean phonePermission
) {
}
