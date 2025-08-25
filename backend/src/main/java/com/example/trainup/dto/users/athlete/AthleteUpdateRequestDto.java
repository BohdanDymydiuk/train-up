package com.example.trainup.dto.users.athlete;

import com.example.trainup.model.enums.Gender;
import java.time.LocalDate;
import java.util.Set;

public record AthleteUpdateRequestDto(
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dateOfBirth,
        Set<String> phoneNumbers,
        Set<Long> sportIds,
        boolean emailPermission,
        boolean phonePermission
) {
}
