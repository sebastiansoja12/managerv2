package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record OpeningScheduleException(
        LocalDate date,
        DayAvailability availability,
        List<OpeningInterval> intervals) {

    public OpeningScheduleException {
        Objects.requireNonNull(date, "Opening schedule exception date cannot be null");
        Objects.requireNonNull(availability, "Opening schedule exception availability cannot be null");
        intervals = List.copyOf(Objects.requireNonNull(intervals, "Opening schedule exception intervals cannot be null"));
        OpeningDay.validateIntervals(availability, intervals);
    }

    public boolean contains(final int minuteOfDay) {
        return switch (this.availability) {
            case CLOSED -> false;
            case ALL_DAY -> true;
            case INTERVALS -> this.intervals.stream().anyMatch(interval -> interval.contains(minuteOfDay));
        };
    }
}
