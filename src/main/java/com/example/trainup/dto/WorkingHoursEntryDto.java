package com.example.trainup.dto;

public record WorkingHoursEntryDto(
        String dayOfWeek,
        String startTime,
        String endTime
) {
}
