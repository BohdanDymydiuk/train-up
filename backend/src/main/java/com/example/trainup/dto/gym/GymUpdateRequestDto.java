package com.example.trainup.dto.gym;

import com.example.trainup.model.WorkingHoursEntry;
import java.util.Set;

public record GymUpdateRequestDto(
        String name,
        GymAddressDto location,
        Set<Long> sportIds,
        String description,
        String website,
        Set<String> phoneNumbers,
        Set<WorkingHoursEntry> workingHours,
        Set<Long> trainerIds,
        Set<String> photoUrls
) {
}
