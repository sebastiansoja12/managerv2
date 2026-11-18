package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.enumeration.ForecastStatus;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.analytics.domain.vo.DailyObservation;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.DepartmentForecast;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

public class ShipmentVolumeForecaster {

    public static final int OBSERVATION_WEEKS = 8;

    public static final int REQUIRED_OBSERVATIONS = 4;

    public static final int MAX_HORIZON_DAYS = 90;

    private static final int AVERAGE_SCALE = 2;

    public List<LocalDate> observationDays(final LocalDate targetDate,
                                           final LocalDate today,
                                           final Optional<LocalDate> historyStart) {
        if (targetDate.isAfter(today.plusDays(MAX_HORIZON_DAYS))) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Shipment forecast is available up to " + MAX_HORIZON_DAYS + " days ahead");
        }
        final LocalDate latestCompleteDay = today.minusDays(1);
        LocalDate latestObservation = targetDate.minusWeeks(1);
        while (latestObservation.isAfter(latestCompleteDay)) {
            latestObservation = latestObservation.minusWeeks(1);
        }
        final LocalDate newestObservation = latestObservation;
        return historyStart
                .map(start -> IntStream.range(0, OBSERVATION_WEEKS)
                        .mapToObj(newestObservation::minusWeeks)
                        .filter(day -> !day.isBefore(start))
                        .sorted()
                        .toList())
                .orElse(List.of());
    }

    public ShipmentVolumeForecast forecast(final LocalDate targetDate,
                                           final LocalDate today,
                                           final List<LocalDate> observationDays,
                                           final List<AnalyticsDepartment> departments,
                                           final List<DailyRegistrations> dailyRegistrations) {
        final ForecastStatus status = observationDays.size() >= REQUIRED_OBSERVATIONS
                ? ForecastStatus.AVAILABLE
                : ForecastStatus.INSUFFICIENT_HISTORY;
        final boolean targetDayCompleted = targetDate.isBefore(today);

        final Map<LocalDate, Long> organizationCounts = new HashMap<>();
        final Map<DepartmentId, Map<LocalDate, Long>> departmentCounts = new HashMap<>();
        dailyRegistrations.forEach(registrations -> {
            organizationCounts.merge(registrations.day(), registrations.registeredShipments(), Long::sum);
            if (registrations.originDepartmentId() != null) {
                departmentCounts.computeIfAbsent(registrations.originDepartmentId(), key -> new HashMap<>())
                        .merge(registrations.day(), registrations.registeredShipments(), Long::sum);
            }
        });

        final List<DepartmentForecast> departmentForecasts = departments.stream()
                .sorted(Comparator.comparing(department -> department.departmentCode().value()))
                .map(department -> departmentForecast(department.departmentCode(), department.city(), status,
                        observationDays, departmentCounts.getOrDefault(department.departmentId(), Map.of()),
                        targetDate, targetDayCompleted))
                .toList();

        return new ShipmentVolumeForecast(targetDate, status, observationDays, REQUIRED_OBSERVATIONS,
                departmentForecast(null, null, status, observationDays, organizationCounts, targetDate,
                        targetDayCompleted),
                departmentForecasts);
    }

    private DepartmentForecast departmentForecast(final DepartmentCode departmentCode,
                                                  final String city,
                                                  final ForecastStatus status,
                                                  final List<LocalDate> observationDays,
                                                  final Map<LocalDate, Long> counts,
                                                  final LocalDate targetDate,
                                                  final boolean targetDayCompleted) {
        final Long actualShipments = targetDayCompleted ? counts.getOrDefault(targetDate, 0L) : null;
        final List<DailyObservation> dailyObservations = observationDays.stream()
                .map(day -> new DailyObservation(day, counts.getOrDefault(day, 0L)))
                .toList();
        if (ForecastStatus.INSUFFICIENT_HISTORY.equals(status)) {
            return new DepartmentForecast(departmentCode, city, null, null, null, null, actualShipments,
                    dailyObservations);
        }
        final List<Long> observations = dailyObservations.stream()
                .map(DailyObservation::registeredShipments)
                .toList();
        final BigDecimal average = BigDecimal.valueOf(observations.stream().mapToLong(Long::longValue).sum())
                .divide(BigDecimal.valueOf(observations.size()), AVERAGE_SCALE, RoundingMode.HALF_UP);
        return new DepartmentForecast(departmentCode, city, average,
                average.setScale(0, RoundingMode.HALF_UP).longValue(),
                observations.stream().mapToLong(Long::longValue).min().orElse(0L),
                observations.stream().mapToLong(Long::longValue).max().orElse(0L),
                actualShipments, dailyObservations);
    }
}
