package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record OpeningSchedule(
        ZoneId timeZone,
        OpeningScheduleMode mode,
        List<OpeningDay> days,
        List<OpeningScheduleException> exceptions) {

    public OpeningSchedule {
        Objects.requireNonNull(timeZone, "Opening schedule time zone cannot be null");
        Objects.requireNonNull(mode, "Opening schedule mode cannot be null");
        days = List.copyOf(Objects.requireNonNull(days, "Opening schedule days cannot be null"));
        exceptions = List.copyOf(Objects.requireNonNull(exceptions, "Opening schedule exceptions cannot be null"));
        validateDays(mode, days);
        validateExceptions(exceptions);
    }

    public boolean isOpenAt(final Instant timestamp) {
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        final ZonedDateTime localTime = timestamp.atZone(this.timeZone);
        final int minuteOfDay = localTime.getHour() * 60 + localTime.getMinute();
        final OpeningScheduleException scheduleException = exceptionsByDate().get(localTime.toLocalDate());
        if (scheduleException != null) {
            return scheduleException.contains(minuteOfDay);
        }
        if (this.mode == OpeningScheduleMode.ALWAYS_OPEN) {
            return true;
        }
        return daysByWeekday().get(localTime.getDayOfWeek()).contains(minuteOfDay);
    }

    private static void validateDays(final OpeningScheduleMode mode, final List<OpeningDay> days) {
        if (mode == OpeningScheduleMode.ALWAYS_OPEN && !days.isEmpty()) {
            throw new InvalidPickupPointConfigurationException(
                    "Always-open schedule cannot contain weekly opening days");
        }
        final Map<DayOfWeek, OpeningDay> indexedDays = new EnumMap<>(DayOfWeek.class);
        for (final OpeningDay day : days) {
            if (indexedDays.put(day.dayOfWeek(), day) != null) {
                throw new InvalidPickupPointConfigurationException(
                        "Opening schedule cannot contain a duplicate weekday");
            }
        }
        if (mode == OpeningScheduleMode.WEEKLY && indexedDays.size() != DayOfWeek.values().length) {
            throw new InvalidPickupPointConfigurationException(
                    "Weekly opening schedule must define all seven weekdays");
        }
    }

    private static void validateExceptions(final List<OpeningScheduleException> exceptions) {
        final Map<LocalDate, OpeningScheduleException> indexedExceptions = new HashMap<>();
        for (final OpeningScheduleException scheduleException : exceptions) {
            if (indexedExceptions.put(scheduleException.date(), scheduleException) != null) {
                throw new InvalidPickupPointConfigurationException(
                        "Opening schedule cannot contain duplicate exception dates");
            }
        }
    }

    private Map<DayOfWeek, OpeningDay> daysByWeekday() {
        final Map<DayOfWeek, OpeningDay> indexedDays = new EnumMap<>(DayOfWeek.class);
        for (final OpeningDay day : this.days) {
            indexedDays.put(day.dayOfWeek(), day);
        }
        return indexedDays;
    }

    private Map<LocalDate, OpeningScheduleException> exceptionsByDate() {
        final Map<LocalDate, OpeningScheduleException> indexedExceptions = new HashMap<>();
        for (final OpeningScheduleException scheduleException : this.exceptions) {
            indexedExceptions.put(scheduleException.date(), scheduleException);
        }
        return indexedExceptions;
    }
}
