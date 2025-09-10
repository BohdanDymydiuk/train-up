package com.example.trainup.dto.gym;

import com.example.trainup.dto.AddressDto;
import com.example.trainup.model.WorkingHoursEntry;
import java.util.Set;

public record GymResponseDto(
        Long id,
        String name,
        AddressDto location,
        Set<Long> sportIds,
        String description,
        String website,
        Set<String> phoneNumbers,
        Set<WorkingHoursEntry> workingHours,
        Set<Long> trainerIds,
        Float overallRating,
        Integer numberOfReviews,
        Long gymOwnerId,
        Set<String> photoUrls
) {
}
