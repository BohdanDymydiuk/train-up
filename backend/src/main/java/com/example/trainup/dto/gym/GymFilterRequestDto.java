package com.example.trainup.dto.gym;

import java.util.Set;

public record GymFilterRequestDto(
        String name,
        String locationCountry,
        String locationCity,
        String locationCityDistrict,
        String locationStreet,
        String locationHouse,
        Set<Long> sportIds,
        Set<Long> trainerIds,
        Float overallRating
) {
}
