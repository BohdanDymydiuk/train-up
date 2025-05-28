package com.example.trainup.dto.users.trainer;

import com.example.trainup.model.enums.Gender;
import java.util.Set;

public record TrainerFilterRequestDto(
        String firstName,
        String lastName,
        Gender gender,
        Set<Long> sportIds,
        Set<Long> gymIds,
        String locationCountry,
        String locationCity,
        String locationCityDistrict,
        String locationStreet,
        String locationHouse,
        Boolean onlineTraining
) {
}
