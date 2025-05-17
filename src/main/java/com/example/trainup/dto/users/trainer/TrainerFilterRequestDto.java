package com.example.trainup.dto.users.trainer;

import java.util.Set;

public record TrainerFilterRequestDto(
        String firstName,
        String lastName,
        Character maleOrFemale,
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
