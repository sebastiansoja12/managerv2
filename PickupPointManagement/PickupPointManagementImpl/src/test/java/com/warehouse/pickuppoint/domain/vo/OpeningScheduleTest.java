package com.warehouse.pickuppoint.domain.vo;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;
import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import com.warehouse.pickuppoint.domain.exception.InvalidPickupPointConfigurationException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpeningScheduleTest {

    @Test
    void shouldUseWeeklyIntervalsInPickupPointTimeZone() {
        final OpeningSchedule schedule = weeklySchedule(List.of());

        assertTrue(schedule.isOpenAt(Instant.parse("2026-09-07T07:30:00Z")));
        assertFalse(schedule.isOpenAt(Instant.parse("2026-09-07T16:30:00Z")));
    }

    @Test
    void shouldReplaceWeeklyDayWithDatedException() {
        final OpeningSchedule schedule = weeklySchedule(List.of(
                new OpeningScheduleException(
                        LocalDate.of(2026, 9, 7),
                        DayAvailability.CLOSED,
                        List.of())));

        assertFalse(schedule.isOpenAt(Instant.parse("2026-09-07T07:30:00Z")));
    }

    @Test
    void shouldRejectOverlappingOpeningIntervals() {
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> new OpeningDay(
                        DayOfWeek.MONDAY,
                        DayAvailability.INTERVALS,
                        List.of(new OpeningInterval(480, 720), new OpeningInterval(700, 900))));
    }

    @Test
    void shouldRequireEveryWeekdayInWeeklySchedule() {
        assertThrows(
                InvalidPickupPointConfigurationException.class,
                () -> new OpeningSchedule(
                        ZoneId.of("Europe/Warsaw"),
                        OpeningScheduleMode.WEEKLY,
                        List.of(closed(DayOfWeek.MONDAY)),
                        List.of()));
    }

    private static OpeningSchedule weeklySchedule(final List<OpeningScheduleException> exceptions) {
        final List<OpeningDay> days = Arrays.stream(DayOfWeek.values())
                .map(dayOfWeek -> dayOfWeek == DayOfWeek.MONDAY
                        ? new OpeningDay(
                                dayOfWeek,
                                DayAvailability.INTERVALS,
                                List.of(new OpeningInterval(480, 1080)))
                        : closed(dayOfWeek))
                .toList();
        return new OpeningSchedule(
                ZoneId.of("Europe/Warsaw"),
                OpeningScheduleMode.WEEKLY,
                days,
                exceptions);
    }

    private static OpeningDay closed(final DayOfWeek dayOfWeek) {
        return new OpeningDay(dayOfWeek, DayAvailability.CLOSED, List.of());
    }
}
