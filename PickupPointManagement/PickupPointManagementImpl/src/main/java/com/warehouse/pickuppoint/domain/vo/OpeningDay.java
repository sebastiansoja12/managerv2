package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record OpeningDay(
        DayOfWeek dayOfWeek,
        DayAvailability availability,
        List<OpeningInterval> intervals) {

    public OpeningDay {
        Objects.requireNonNull(dayOfWeek, "Day of week cannot be null");
        Objects.requireNonNull(availability, "Day availability cannot be null");
        intervals = List.copyOf(Objects.requireNonNull(intervals, "Opening intervals cannot be null"));
        validateIntervals(availability, intervals);
    }

    public boolean contains(final int minuteOfDay) {
        return switch (this.availability) {
            case CLOSED -> false;
            case ALL_DAY -> true;
            case INTERVALS -> this.intervals.stream().anyMatch(interval -> interval.contains(minuteOfDay));
        };
    }

    static void validateIntervals(
            final DayAvailability availability,
            final List<OpeningInterval> intervals) {
        if (availability == DayAvailability.INTERVALS && intervals.isEmpty()) {
            throw new InvalidPickupPointConfigurationException(
                    "Interval-based opening day must contain at least one interval");
        }
        if (availability != DayAvailability.INTERVALS && !intervals.isEmpty()) {
            throw new InvalidPickupPointConfigurationException(
                    "Opening intervals are allowed only for interval-based days");
        }
        final List<OpeningInterval> sortedIntervals = intervals.stream()
                .sorted(Comparator.comparingInt(OpeningInterval::startMinute))
                .toList();
        for (int index = 1; index < sortedIntervals.size(); index++) {
            if (sortedIntervals.get(index).startMinute() < sortedIntervals.get(index - 1).endMinute()) {
                throw new InvalidPickupPointConfigurationException("Opening intervals cannot overlap");
            }
        }
    }
}
