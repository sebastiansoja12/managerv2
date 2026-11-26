package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.ForecastQuery;
import com.warehouse.analytics.application.port.primary.command.PerformanceQuery;
import com.warehouse.analytics.application.port.secondary.DepartmentCostRepository;
import com.warehouse.analytics.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.analytics.application.port.secondary.IncidentStatisticsServicePort;
import com.warehouse.analytics.application.port.secondary.ShipmentStatisticsServicePort;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.service.DepartmentPerformanceCalculator;
import com.warehouse.analytics.domain.service.ShipmentVolumeForecaster;
import com.warehouse.analytics.domain.vo.AnalyticsDepartment;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.analytics.domain.vo.ShipmentFigures;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;
import com.warehouse.commonassets.identificator.DepartmentCode;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrganizationAnalyticsPortImpl implements OrganizationAnalyticsPort {

    private final ShipmentStatisticsServicePort shipmentStatisticsServicePort;

    private final IncidentStatisticsServicePort incidentStatisticsServicePort;

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    private final DepartmentCostRepository departmentCostRepository;

    private final DepartmentPerformanceCalculator departmentPerformanceCalculator;

    private final ShipmentVolumeForecaster shipmentVolumeForecaster;

    private final Clock clock;

    public OrganizationAnalyticsPortImpl(final ShipmentStatisticsServicePort shipmentStatisticsServicePort,
                                         final IncidentStatisticsServicePort incidentStatisticsServicePort,
                                         final DepartmentDirectoryServicePort departmentDirectoryServicePort,
                                         final DepartmentCostRepository departmentCostRepository,
                                         final DepartmentPerformanceCalculator departmentPerformanceCalculator,
                                         final ShipmentVolumeForecaster shipmentVolumeForecaster,
                                         final Clock clock) {
        this.shipmentStatisticsServicePort = shipmentStatisticsServicePort;
        this.incidentStatisticsServicePort = incidentStatisticsServicePort;
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.departmentCostRepository = departmentCostRepository;
        this.departmentPerformanceCalculator = departmentPerformanceCalculator;
        this.shipmentVolumeForecaster = shipmentVolumeForecaster;
        this.clock = clock;
    }

    @Override
    @Transactional(readOnly = true)
    public PerformanceReport performance(final PerformanceQuery query) {
        final List<AnalyticsDepartment> departments = this.departmentDirectoryServicePort.departments();
        requireKnownDepartments(departments, query.departmentCodes());
        final ShipmentFigures shipmentFigures = this.shipmentStatisticsServicePort.shipmentFigures(query.period());
        return this.departmentPerformanceCalculator.calculate(query.period(), query.currency(),
                query.departmentCodes(), departments, shipmentFigures.registrations(), shipmentFigures.deliveries(),
                this.incidentStatisticsServicePort.incidentFigures(query.period()),
                this.departmentCostRepository.find(query.period(), Optional.empty()));
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentVolumeForecast forecast(final ForecastQuery query) {
        final LocalDate today = LocalDate.now(this.clock);
        final List<AnalyticsDepartment> departments = this.departmentDirectoryServicePort.departments();
        query.departmentCode().ifPresent(departmentCode -> requireKnownDepartments(departments,
                Set.of(departmentCode)));
        final List<LocalDate> observationDays = this.shipmentVolumeForecaster.observationDays(query.targetDate(),
                today, this.shipmentStatisticsServicePort.firstRegistrationDate());
        final List<LocalDate> requestedDays = Stream.concat(observationDays.stream(),
                        query.targetDate().isBefore(today) ? Stream.of(query.targetDate()) : Stream.empty())
                .sorted()
                .toList();
        final List<DailyRegistrations> dailyRegistrations = requestedDays.isEmpty()
                ? List.of()
                : this.shipmentStatisticsServicePort.dailyRegistrations(requestedDays.get(0),
                        requestedDays.get(requestedDays.size() - 1));
        return this.shipmentVolumeForecaster.forecast(query.targetDate(), today, observationDays,
                query.departmentCode()
                        .map(departmentCode -> departments.stream()
                                .filter(department -> department.departmentCode().equals(departmentCode))
                                .toList())
                        .orElse(departments),
                dailyRegistrations);
    }

    private void requireKnownDepartments(final List<AnalyticsDepartment> departments,
                                         final Collection<DepartmentCode> departmentCodes) {
        final Set<DepartmentCode> knownCodes = departments.stream()
                .map(AnalyticsDepartment::departmentCode)
                .collect(Collectors.toSet());
        departmentCodes.stream()
                .filter(departmentCode -> !knownCodes.contains(departmentCode))
                .findFirst()
                .ifPresent(departmentCode -> {
                    throw new AnalyticsException(AnalyticsException.Reason.NOT_FOUND,
                            "Department " + departmentCode.value() + " does not exist");
                });
    }
}
