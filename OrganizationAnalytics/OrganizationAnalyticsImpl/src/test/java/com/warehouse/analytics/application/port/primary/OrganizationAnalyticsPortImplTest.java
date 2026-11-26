package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.ForecastQuery;
import com.warehouse.analytics.application.port.primary.command.PerformanceQuery;
import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.application.port.secondary.IncidentStatisticsServicePort;
import com.warehouse.analytics.application.port.secondary.ShipmentStatisticsServicePort;
import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.enumeration.ForecastStatus;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.service.DepartmentPerformanceCalculator;
import com.warehouse.analytics.domain.service.ShipmentVolumeForecaster;
import com.warehouse.analytics.domain.vo.IncidentFigures;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.analytics.domain.vo.ShipmentFigures;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.warehouse.analytics.AnalyticsFixture.GLIWICE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_CODE;
import static com.warehouse.analytics.AnalyticsFixture.KATOWICE_ID;
import static com.warehouse.analytics.AnalyticsFixture.NOVEMBER;
import static com.warehouse.analytics.AnalyticsFixture.cost;
import static com.warehouse.analytics.AnalyticsFixture.daily;
import static com.warehouse.analytics.AnalyticsFixture.deliveries;
import static com.warehouse.analytics.AnalyticsFixture.registrations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrganizationAnalyticsPortImplTest {

    private static final Clock CLOCK = Clock.fixed(LocalDate.of(2026, 11, 17).atStartOfDay()
            .toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private final ShipmentStatisticsServicePort shipmentStatisticsServicePort =
            mock(ShipmentStatisticsServicePort.class);

    private final IncidentStatisticsServicePort incidentStatisticsServicePort =
            mock(IncidentStatisticsServicePort.class);

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort =
            mock(DepartmentDirectoryServicePort.class);

    private final DepartmentCostRepository departmentCostRepository = mock(DepartmentCostRepository.class);

    private OrganizationAnalyticsPortImpl port;

    @BeforeEach
    void setUp() {
        this.port = new OrganizationAnalyticsPortImpl(shipmentStatisticsServicePort, incidentStatisticsServicePort,
                departmentDirectoryServicePort, departmentCostRepository, new DepartmentPerformanceCalculator(),
                new ShipmentVolumeForecaster(), CLOCK);
        when(departmentDirectoryServicePort.departments()).thenReturn(List.of(KATOWICE, GLIWICE));
    }

    @Test
    void shouldCombineShipmentIncidentAndCostSourcesIntoPerformanceReport() {
        when(shipmentStatisticsServicePort.shipmentFigures(NOVEMBER)).thenReturn(new ShipmentFigures(
                List.of(registrations(KATOWICE_ID, Currency.PLN, 4, 0, "400.00")),
                List.of(deliveries(KATOWICE_ID, Currency.PLN, 2, "200.00"))));
        when(incidentStatisticsServicePort.incidentFigures(NOVEMBER))
                .thenReturn(List.of(new IncidentFigures(KATOWICE_CODE, 1, 1)));
        when(departmentCostRepository.find(NOVEMBER, Optional.empty()))
                .thenReturn(List.of(cost(KATOWICE_CODE, DepartmentCostCategory.FUEL, "100.00", Currency.PLN)));

        final PerformanceReport report = port.performance(new PerformanceQuery(NOVEMBER, Set.of(), null));

        assertThat(report.currency()).isEqualTo(Currency.PLN);
        assertThat(report.organization().revenue()).isEqualByComparingTo("400.00");
        assertThat(report.organization().actualTurnover()).isEqualByComparingTo("200.00");
        assertThat(report.organization().financialResult()).isEqualByComparingTo("300.00");
        assertThat(report.organization().damageClaims()).isEqualTo(1);
        assertThat(report.departments()).hasSize(2);
    }

    @Test
    void shouldRejectPerformanceQueryForUnknownDepartment() {
        assertThatThrownBy(() -> port.performance(new PerformanceQuery(NOVEMBER,
                Set.of(new DepartmentCode("XXX")), Currency.PLN)))
                .isInstanceOf(AnalyticsException.class)
                .hasMessageContaining("XXX");
        verify(shipmentStatisticsServicePort, never()).shipmentFigures(any());
    }

    @Test
    void shouldLoadDailyRegistrationsForObservationWindowAndCompletedTargetDay() {
        final LocalDate targetDate = LocalDate.of(2026, 11, 10);
        when(shipmentStatisticsServicePort.firstRegistrationDate()).thenReturn(Optional.of(LocalDate.of(2026, 1, 1)));
        when(shipmentStatisticsServicePort.dailyRegistrations(LocalDate.of(2026, 9, 15), targetDate))
                .thenReturn(List.of(daily(KATOWICE_ID, LocalDate.of(2026, 11, 3), 8)));

        final ShipmentVolumeForecast forecast = port.forecast(new ForecastQuery(targetDate,
                Optional.of(KATOWICE_CODE)));

        assertThat(forecast.status()).isEqualTo(ForecastStatus.AVAILABLE);
        assertThat(forecast.departments()).extracting(department -> department.departmentCode().value())
                .containsExactly("KAT");
        assertThat(forecast.departments().get(0).averageShipments()).isEqualByComparingTo("1.00");
        assertThat(forecast.departments().get(0).actualShipments()).isZero();
    }

    @Test
    void shouldNotQueryDailyRegistrationsWhenThereIsNoShipmentHistory() {
        when(shipmentStatisticsServicePort.firstRegistrationDate()).thenReturn(Optional.empty());

        final ShipmentVolumeForecast forecast = port.forecast(new ForecastQuery(LocalDate.of(2026, 11, 20),
                Optional.empty()));

        assertThat(forecast.status()).isEqualTo(ForecastStatus.INSUFFICIENT_HISTORY);
        assertThat(forecast.observationDays()).isEmpty();
        verify(shipmentStatisticsServicePort, never()).dailyRegistrations(any(), any());
    }
}
