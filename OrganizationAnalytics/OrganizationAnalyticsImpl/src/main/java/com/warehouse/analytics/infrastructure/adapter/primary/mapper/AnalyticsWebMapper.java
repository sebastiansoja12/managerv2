package com.warehouse.analytics.infrastructure.adapter.primary.mapper;

import com.warehouse.analytics.application.port.primary.command.FindDepartmentCostsCommand;
import com.warehouse.analytics.application.port.primary.command.ForecastQuery;
import com.warehouse.analytics.application.port.primary.command.PerformanceQuery;
import com.warehouse.analytics.application.port.primary.command.RecordDepartmentCostCommand;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.CostBreakdown;
import com.warehouse.analytics.domain.vo.DepartmentForecast;
import com.warehouse.analytics.domain.vo.DepartmentPerformance;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;
import com.warehouse.analytics.infrastructure.adapter.primary.api.CostBreakdownApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DailyObservationApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentCostApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentCostIdDto;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentForecastApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentPerformanceApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.PerformanceReportApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.RecordDepartmentCostApiRequest;
import com.warehouse.analytics.infrastructure.adapter.primary.api.ShipmentForecastApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.UserIdDto;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalyticsWebMapper {

    public PerformanceQuery toPerformanceQuery(final LocalDate dateFrom,
                                               final LocalDate dateTo,
                                               final List<String> departmentCodes,
                                               final Currency currency) {
        final Set<DepartmentCode> codes = departmentCodes == null
                ? Set.of()
                : departmentCodes.stream()
                        .filter(code -> !code.isBlank())
                        .map(code -> new DepartmentCode(code.trim()))
                        .collect(Collectors.toUnmodifiableSet());
        return new PerformanceQuery(new AnalyticsPeriod(dateFrom, dateTo), codes, currency);
    }

    public ForecastQuery toForecastQuery(final LocalDate targetDate, final String departmentCode) {
        return new ForecastQuery(targetDate, departmentCode(departmentCode));
    }

    public FindDepartmentCostsCommand toFindCostsCommand(final LocalDate dateFrom,
                                                         final LocalDate dateTo,
                                                         final String departmentCode) {
        return new FindDepartmentCostsCommand(new AnalyticsPeriod(dateFrom, dateTo), departmentCode(departmentCode));
    }

    public RecordDepartmentCostCommand toCommand(final RecordDepartmentCostApiRequest request) {
        return new RecordDepartmentCostCommand(new DepartmentCode(request.departmentCode().trim()),
                request.category(), request.amount(), request.currency(), request.incurredOn(),
                request.description());
    }

    public PerformanceReportApiResponse toResponse(final PerformanceReport report) {
        return new PerformanceReportApiResponse(report.period().dateFrom(), report.period().dateTo(),
                report.currency(), report.availableCurrencies(), toResponse(report.organization()),
                report.unassigned().map(this::toResponse).orElse(null),
                report.departments().stream().map(this::toResponse).toList());
    }

    public ShipmentForecastApiResponse toResponse(final ShipmentVolumeForecast forecast) {
        return new ShipmentForecastApiResponse(forecast.targetDate(), forecast.status(), forecast.observationDays(),
                forecast.requiredObservations(), toResponse(forecast.organization()),
                forecast.departments().stream().map(this::toResponse).toList());
    }

    public List<DepartmentCostApiResponse> toResponse(final List<DepartmentCost> departmentCosts) {
        return departmentCosts.stream()
                .map(cost -> new DepartmentCostApiResponse(new DepartmentCostIdDto(cost.costId().value()),
                        cost.departmentCode().value(), cost.category(), cost.amount(), cost.currency(),
                        cost.incurredOn(), cost.description(), new UserIdDto(cost.recordedBy().value()),
                        cost.recordedAt()))
                .toList();
    }

    public DepartmentCostIdDto toResponse(final DepartmentCostId costId) {
        return new DepartmentCostIdDto(costId.value());
    }

    private DepartmentPerformanceApiResponse toResponse(final DepartmentPerformance performance) {
        final CostBreakdown costs = performance.costs();
        return new DepartmentPerformanceApiResponse(
                Optional.ofNullable(performance.departmentCode()).map(DepartmentCode::value).orElse(null),
                performance.city(), performance.registeredShipments(), performance.canceledShipments(),
                performance.unpricedShipments(), performance.deliveredShipments(), performance.revenue(),
                performance.actualTurnover(),
                new CostBreakdownApiResponse(costs.fuel(), costs.shipmentDamage(), costs.claims(), costs.total()),
                performance.costEntries(), performance.financialResult(), performance.profitabilityPercent(),
                performance.revenueSharePercent(), performance.damagedShipments(), performance.damageClaims());
    }

    private DepartmentForecastApiResponse toResponse(final DepartmentForecast forecast) {
        return new DepartmentForecastApiResponse(
                Optional.ofNullable(forecast.departmentCode()).map(DepartmentCode::value).orElse(null),
                forecast.city(), forecast.averageShipments(), forecast.expectedShipments(),
                forecast.minimumObserved(), forecast.maximumObserved(), forecast.actualShipments(),
                forecast.observations().stream()
                        .map(observation -> new DailyObservationApiResponse(observation.day(),
                                observation.registeredShipments()))
                        .toList());
    }

    private Optional<DepartmentCode> departmentCode(final String departmentCode) {
        return Optional.ofNullable(departmentCode)
                .filter(code -> !code.isBlank())
                .map(code -> new DepartmentCode(code.trim()));
    }
}
