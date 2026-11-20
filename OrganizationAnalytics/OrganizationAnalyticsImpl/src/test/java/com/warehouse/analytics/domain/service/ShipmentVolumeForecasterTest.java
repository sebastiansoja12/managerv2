package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.enumeration.ForecastStatus;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.vo.DailyObservation;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.DepartmentForecast;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.warehouse.analytics.AnalyticsFixture.GLIWICE;
import static com.warehouse.analytics.AnalyticsFixture.GLIWICE_ID;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_ID;
import static com.warehouse.analytics.AnalyticsFixture.daily;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShipmentVolumeForecasterTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 11, 17);

    private static final LocalDate NEXT_TUESDAY = LocalDate.of(2026, 11, 24);

    private final ShipmentVolumeForecaster forecaster = new ShipmentVolumeForecaster();

    @Test
    void shouldUseEightCompletedSameWeekdaysBeforeTargetDate() {
        final List<LocalDate> days = forecaster.observationDays(NEXT_TUESDAY, TODAY,
                Optional.of(LocalDate.of(2026, 1, 1)));

        assertThat(days).containsExactly(
                LocalDate.of(2026, 9, 22), LocalDate.of(2026, 9, 29), LocalDate.of(2026, 10, 6),
                LocalDate.of(2026, 10, 13), LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 27),
                LocalDate.of(2026, 11, 3), LocalDate.of(2026, 11, 10));
    }

    @Test
    void shouldUsePrecedingWeeksForPastTargetDate() {
        final List<LocalDate> days = forecaster.observationDays(LocalDate.of(2026, 11, 3), TODAY,
                Optional.of(LocalDate.of(2026, 1, 1)));

        assertThat(days).hasSize(8).last().isEqualTo(LocalDate.of(2026, 10, 27));
    }

    @Test
    void shouldSkipDaysBeforeFirstRegisteredShipment() {
        final List<LocalDate> days = forecaster.observationDays(NEXT_TUESDAY, TODAY,
                Optional.of(LocalDate.of(2026, 10, 15)));

        assertThat(days).containsExactly(LocalDate.of(2026, 10, 20), LocalDate.of(2026, 10, 27),
                LocalDate.of(2026, 11, 3), LocalDate.of(2026, 11, 10));
    }

    @Test
    void shouldReturnNoObservationDaysWithoutShipmentHistory() {
        assertThat(forecaster.observationDays(NEXT_TUESDAY, TODAY, Optional.empty())).isEmpty();
    }

    @Test
    void shouldRejectTargetDateBeyondForecastHorizon() {
        assertThatThrownBy(() -> forecaster.observationDays(TODAY.plusDays(91), TODAY, Optional.of(TODAY)))
                .isInstanceOfSatisfying(AnalyticsException.class,
                        exception -> assertThat(exception.reason()).isEqualTo(AnalyticsException.Reason.INVALID_REQUEST));
    }

    @Test
    void shouldForecastAverageAndObservedRangePerDepartment() {
        final List<LocalDate> days = forecaster.observationDays(NEXT_TUESDAY, TODAY,
                Optional.of(LocalDate.of(2026, 10, 15)));
        final List<DailyRegistrations> registrations = new ArrayList<>();
        final long[] katowiceCounts = {10, 12, 14, 16};
        for (int index = 0; index < days.size(); index++) {
            registrations.add(daily(KATOWICE_ID, days.get(index), katowiceCounts[index]));
        }
        registrations.add(daily(GLIWICE_ID, days.get(1), 4));
        registrations.add(daily(null, days.get(2), 2));
        registrations.add(daily(KATOWICE_ID, days.get(2).plusDays(1), 100));

        final ShipmentVolumeForecast forecast = forecaster.forecast(NEXT_TUESDAY, TODAY, days,
                List.of(KATOWICE, GLIWICE), registrations);

        final DepartmentForecast katowice = forecast.departments().stream()
                .filter(department -> department.departmentCode().value().equals("KAT"))
                .findFirst().orElseThrow();
        final DepartmentForecast gliwice = forecast.departments().stream()
                .filter(department -> department.departmentCode().value().equals("GLI"))
                .findFirst().orElseThrow();

        assertThat(forecast.status()).isEqualTo(ForecastStatus.AVAILABLE);
        assertThat(katowice.averageShipments()).isEqualByComparingTo("13.00");
        assertThat(katowice.expectedShipments()).isEqualTo(13L);
        assertThat(katowice.minimumObserved()).isEqualTo(10L);
        assertThat(katowice.maximumObserved()).isEqualTo(16L);
        assertThat(katowice.actualShipments()).isNull();
        assertThat(katowice.observations()).extracting(DailyObservation::registeredShipments)
                .containsExactly(10L, 12L, 14L, 16L);
        assertThat(katowice.observations()).extracting(DailyObservation::day).isEqualTo(days);
        assertThat(gliwice.averageShipments()).isEqualByComparingTo("1.00");
        assertThat(gliwice.minimumObserved()).isZero();
        assertThat(forecast.organization().averageShipments()).isEqualByComparingTo("14.50");
        assertThat(forecast.organization().averageShipments()).isEqualByComparingTo(
                katowice.averageShipments().add(gliwice.averageShipments()).add(new BigDecimal("0.50")));
    }

    @Test
    void shouldNotForecastWhenHistoryIsInsufficient() {
        final List<LocalDate> days = List.of(LocalDate.of(2026, 10, 27), LocalDate.of(2026, 11, 3),
                LocalDate.of(2026, 11, 10));

        final ShipmentVolumeForecast forecast = forecaster.forecast(NEXT_TUESDAY, TODAY, days, List.of(KATOWICE),
                List.of(daily(KATOWICE_ID, days.get(0), 20)));

        assertThat(forecast.status()).isEqualTo(ForecastStatus.INSUFFICIENT_HISTORY);
        assertThat(forecast.requiredObservations()).isEqualTo(4);
        assertThat(forecast.organization().averageShipments()).isNull();
        assertThat(forecast.organization().expectedShipments()).isNull();
        assertThat(forecast.departments().get(0).expectedShipments()).isNull();
        assertThat(forecast.departments().get(0).observations()).extracting(DailyObservation::registeredShipments)
                .containsExactly(20L, 0L, 0L);
    }

    @Test
    void shouldReportActualShipmentsForCompletedTargetDay() {
        final LocalDate targetDate = LocalDate.of(2026, 11, 10);
        final List<LocalDate> days = forecaster.observationDays(targetDate, TODAY,
                Optional.of(LocalDate.of(2026, 10, 1)));

        final ShipmentVolumeForecast forecast = forecaster.forecast(targetDate, TODAY, days, List.of(KATOWICE),
                List.of(daily(KATOWICE_ID, targetDate, 9), daily(KATOWICE_ID, days.get(0), 8)));

        assertThat(forecast.departments().get(0).actualShipments()).isEqualTo(9L);
        assertThat(forecast.organization().actualShipments()).isEqualTo(9L);
        assertThat(forecast.departments().get(0).averageShipments()).isEqualByComparingTo("1.60");
    }
}
