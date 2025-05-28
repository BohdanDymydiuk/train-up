package com.example.trainup.dto.users.athlete;

import com.example.trainup.model.enums.Gender;
import java.time.LocalDate;
import java.util.Set;

public record AthleteFilterRequestDto(
        String firstName,
        String lastName,
        Gender gender,
        LocalDate dateOfBirth,
        Set<Long> sportIds,
        Boolean emailPermission,
        Boolean phonePermission
) {
}
