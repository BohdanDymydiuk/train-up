package com.example.trainup.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"dayOfWeek", "startTime", "endTime"})
public class WorkingHoursEntry {
    @Enumerated(EnumType.STRING)
    private DayOfTheWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public enum DayOfTheWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }
}
