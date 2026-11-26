package com.warehouse.logisticsdelivery.domain.service;

import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentWindow;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class AssignmentWindowCalculator {

    public Optional<AssignmentWindow> calculate(final DepartmentAssignmentConfig config,
                                                final Optional<Instant> lastWatermark,
                                                final Instant now) {
        final Instant cutoff = now.minus(config.processingDelay());
        final Instant lookbackFloor = now.minus(config.lookback());
        final Instant configuredStart = configuredStart(config, cutoff, lookbackFloor);
        final Instant configuredEnd = configuredEnd(config, cutoff, configuredStart);
        final Optional<Instant> validWatermark = lastWatermark.filter(watermark -> !watermark.isBefore(lookbackFloor));
        if (validWatermark.isPresent() && !validWatermark.get().isBefore(configuredEnd)) {
            return Optional.empty();
        }
        final Instant from = validWatermark
                .map(watermark -> watermark.minus(config.overlap()))
                .filter(start -> start.isAfter(configuredStart))
                .orElse(configuredStart);
        final Instant to = earliest(from.plus(config.maxWindow()), cutoff, configuredEnd);
        if (!from.isBefore(to)) {
            return Optional.empty();
        }
        return Optional.of(new AssignmentWindow(from, to, lookbackFloor.isBefore(from) ? lookbackFloor : from));
    }

    private Instant configuredStart(final DepartmentAssignmentConfig config, final Instant cutoff,
                                    final Instant lookbackFloor) {
        final ZoneId zone = config.timeZone();
        final LocalDate cutoffDay = cutoff.atZone(zone).toLocalDate();
        if (config.windowType() == AssignmentWindowType.ROLLING) {
            return lookbackFloor;
        }
        if (config.windowType() == AssignmentWindowType.PREVIOUS_OPERATIONAL_DAY) {
            return cutoffDay.minusDays(1).atStartOfDay(zone).toInstant();
        }
        final Instant todayStart = cutoffDay.atTime(config.windowStart()).atZone(zone).toInstant();
        return todayStart.isAfter(cutoff)
                ? cutoffDay.minusDays(1).atTime(config.windowStart()).atZone(zone).toInstant()
                : todayStart;
    }

    private Instant configuredEnd(final DepartmentAssignmentConfig config, final Instant cutoff,
                                  final Instant configuredStart) {
        final ZoneId zone = config.timeZone();
        if (config.windowType() == AssignmentWindowType.ROLLING) {
            return cutoff;
        }
        final LocalDate startDay = configuredStart.atZone(zone).toLocalDate();
        if (config.windowType() == AssignmentWindowType.PREVIOUS_OPERATIONAL_DAY) {
            return startDay.plusDays(1).atStartOfDay(zone).toInstant();
        }
        final LocalDate endDay = config.windowEnd().isAfter(config.windowStart()) ? startDay : startDay.plusDays(1);
        return endDay.atTime(config.windowEnd()).atZone(zone).toInstant();
    }

    private static Instant earliest(final Instant first, final Instant second, final Instant third) {
        final Instant earlier = first.isBefore(second) ? first : second;
        return earlier.isBefore(third) ? earlier : third;
    }
}
