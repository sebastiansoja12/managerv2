package com.warehouse.logisticsdelivery.domain.service;

import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.WARSAW;
import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.config;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class AssignmentWindowCalculatorTest {

    private static final Instant NOW = ZonedDateTime.of(2026, 11, 24, 14, 0, 0, 0, WARSAW).toInstant();
    private static final Instant LOOKBACK_FLOOR = Instant.parse("2026-11-21T13:00:00Z");

    private final AssignmentWindowCalculator calculator = new AssignmentWindowCalculator();

    @Test
    void shouldStartAtConfiguredTimeOfDayWindowWhenThereIsNoPreviousRun() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(8, 0), LocalTime.of(12, 0)), Optional.empty(), NOW);

        assertThat(window, equalTo(Optional.of(new AssignmentWindow(Instant.parse("2026-11-24T07:00:00Z"),
                Instant.parse("2026-11-24T11:00:00Z"), LOOKBACK_FLOOR))));
    }

    @Test
    void shouldUsePreviousDayWindowWhenTodayWindowHasNotStarted() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(16, 0), LocalTime.of(20, 0)), Optional.empty(), NOW);

        assertThat(window, equalTo(Optional.of(new AssignmentWindow(Instant.parse("2026-11-23T15:00:00Z"),
                Instant.parse("2026-11-23T19:00:00Z"), LOOKBACK_FLOOR))));
    }

    @Test
    void shouldReturnEmptyWindowWhenConfiguredWindowIsAlreadyProcessed() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(16, 0), LocalTime.of(20, 0)),
                Optional.of(Instant.parse("2026-11-23T19:00:00Z")), NOW);

        assertThat(window.isEmpty(), is(true));
    }

    @Test
    void shouldStartFromWatermarkMinusOverlap() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                Optional.of(Instant.parse("2026-11-24T09:00:00Z")), NOW);

        assertThat(window.orElseThrow().from(), equalTo(Instant.parse("2026-11-24T08:45:00Z")));
        assertThat(window.orElseThrow().to(), equalTo(Instant.parse("2026-11-24T11:00:00Z")));
    }

    @Test
    void shouldResetToConfiguredStartWhenWatermarkIsOlderThanLookback() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(8, 0), LocalTime.of(12, 0)),
                Optional.of(Instant.parse("2026-11-20T00:00:00Z")), NOW);

        assertThat(window.orElseThrow().from(), equalTo(Instant.parse("2026-11-24T07:00:00Z")));
    }

    @Test
    void shouldCutWindowAtProcessingDelay() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(12, 0), LocalTime.of(16, 0)), Optional.empty(), NOW);

        assertThat(window.orElseThrow().to(), equalTo(Instant.parse("2026-11-24T12:55:00Z")));
    }

    @Test
    void shouldHandleWindowCrossingMidnight() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(22, 0), LocalTime.of(6, 0)), Optional.empty(), NOW);

        assertThat(window.orElseThrow().from(), equalTo(Instant.parse("2026-11-23T21:00:00Z")));
        assertThat(window.orElseThrow().to(), equalTo(Instant.parse("2026-11-24T05:00:00Z")));
    }

    @Test
    void shouldCoverWholePreviousOperationalDay() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.PREVIOUS_OPERATIONAL_DAY, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT),
                Optional.empty(), NOW);

        assertThat(window.orElseThrow().from(), equalTo(Instant.parse("2026-11-22T23:00:00Z")));
        assertThat(window.orElseThrow().to(), equalTo(Instant.parse("2026-11-23T23:00:00Z")));
    }

    @Test
    void shouldUseShorterOperationalDayAcrossDaylightSavingChange() {
        final Instant afterSpringForward = ZonedDateTime.of(2026, 3, 30, 10, 0, 0, 0, WARSAW).toInstant();

        final AssignmentWindow window = calculator.calculate(
                config(AssignmentWindowType.PREVIOUS_OPERATIONAL_DAY, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT),
                Optional.empty(), afterSpringForward).orElseThrow();

        assertThat(window.from(), equalTo(Instant.parse("2026-03-28T23:00:00Z")));
        assertThat(window.to(), equalTo(Instant.parse("2026-03-29T22:00:00Z")));
        assertThat(Duration.between(window.from(), window.to()), equalTo(Duration.ofHours(23)));
    }

    @Test
    void shouldCatchUpRollingBacklogInMaxWindowChunks() {
        final AssignmentWindow window = calculator.calculate(
                config(AssignmentWindowType.ROLLING, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT), Optional.empty(), NOW)
                .orElseThrow();

        assertThat(window.from(), equalTo(LOOKBACK_FLOOR));
        assertThat(window.to(), equalTo(LOOKBACK_FLOOR.plus(Duration.ofHours(24))));
    }

    @Test
    void shouldContinueRollingWindowUpToProcessingCutoff() {
        final AssignmentWindow window = calculator.calculate(
                config(AssignmentWindowType.ROLLING, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT),
                Optional.of(Instant.parse("2026-11-24T12:00:00Z")), NOW).orElseThrow();

        assertThat(window.from(), equalTo(Instant.parse("2026-11-24T11:45:00Z")));
        assertThat(window.to(), equalTo(Instant.parse("2026-11-24T12:55:00Z")));
        assertThat(window.backlogFrom(), equalTo(LOOKBACK_FLOOR));
    }

    @Test
    void shouldReturnEmptyWindowWhenNothingIsLeftBeforeCutoff() {
        final Optional<AssignmentWindow> window = calculator.calculate(
                config(AssignmentWindowType.ROLLING, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT),
                Optional.of(Instant.parse("2026-11-24T13:20:00Z")), NOW);

        assertThat(window.isEmpty(), is(true));
    }

    @Test
    void shouldLimitWindowToMaxWindowHours() {
        final DepartmentAssignmentConfig base = config(AssignmentWindowType.TIME_OF_DAY, LocalTime.of(8, 0),
                LocalTime.of(12, 0));
        final DepartmentAssignmentConfig oneHourChunks = new DepartmentAssignmentConfig(base.departmentId(), true,
                base.dateField(), base.windowType(), base.windowStart(), base.windowEnd(), base.timeZone(),
                base.processingDelay(), base.overlap(), Duration.ofHours(1), base.lookback(), base.batchSize());

        final AssignmentWindow window = calculator.calculate(oneHourChunks, Optional.empty(), NOW).orElseThrow();

        assertThat(window.to(), equalTo(Instant.parse("2026-11-24T08:00:00Z")));
    }
}
