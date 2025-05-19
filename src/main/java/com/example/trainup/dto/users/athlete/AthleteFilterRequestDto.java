package com.example.trainup.dto.users.athlete;

import java.time.LocalDate;
import java.util.Set;

public record AthleteFilterRequestDto(
        String firstName,
        String lastName,
        Character maleOrFemale,
        LocalDate dateOfBirth,
        Set<Long> sportIds,
        Boolean emailPermission,
        Boolean phonePermission
) {
}
