package com.warehouse.analytics.infrastructure.adapter.primary;

import com.warehouse.analytics.application.port.primary.DepartmentCostPort;
import com.warehouse.analytics.application.port.primary.OrganizationAnalyticsPort;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentCostApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.DepartmentCostIdDto;
import com.warehouse.analytics.infrastructure.adapter.primary.api.PerformanceReportApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.api.RecordDepartmentCostApiRequest;
import com.warehouse.analytics.infrastructure.adapter.primary.api.ShipmentForecastApiResponse;
import com.warehouse.analytics.infrastructure.adapter.primary.mapper.AnalyticsWebMapper;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AccessUserControl(minimumLevel = AccountLevel.MANAGER)
@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final OrganizationAnalyticsPort organizationAnalyticsPort;

    private final DepartmentCostPort departmentCostPort;

    private final AnalyticsWebMapper analyticsWebMapper;

    public AnalyticsController(final OrganizationAnalyticsPort organizationAnalyticsPort,
                               final DepartmentCostPort departmentCostPort,
                               final AnalyticsWebMapper analyticsWebMapper) {
        this.organizationAnalyticsPort = organizationAnalyticsPort;
        this.departmentCostPort = departmentCostPort;
        this.analyticsWebMapper = analyticsWebMapper;
    }

    @GetMapping("/performance")
    public ResponseEntity<PerformanceReportApiResponse> performance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final List<String> departmentCode,
            @RequestParam(required = false) final Currency currency) {
        return ResponseEntity.ok(this.analyticsWebMapper.toResponse(this.organizationAnalyticsPort.performance(
                this.analyticsWebMapper.toPerformanceQuery(dateFrom, dateTo, departmentCode, currency))));
    }

    @GetMapping("/forecast")
    public ResponseEntity<ShipmentForecastApiResponse> forecast(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
            @RequestParam(required = false) final String departmentCode) {
        return ResponseEntity.ok(this.analyticsWebMapper.toResponse(this.organizationAnalyticsPort.forecast(
                this.analyticsWebMapper.toForecastQuery(date, departmentCode))));
    }

    @GetMapping("/costs")
    public ResponseEntity<List<DepartmentCostApiResponse>> costs(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateFrom,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dateTo,
            @RequestParam(required = false) final String departmentCode) {
        return ResponseEntity.ok(this.analyticsWebMapper.toResponse(this.departmentCostPort.find(
                this.analyticsWebMapper.toFindCostsCommand(dateFrom, dateTo, departmentCode))));
    }

    @PostMapping("/costs")
    public ResponseEntity<DepartmentCostIdDto> recordCost(
            @Valid @RequestBody final RecordDepartmentCostApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.analyticsWebMapper.toResponse(
                this.departmentCostPort.record(this.analyticsWebMapper.toCommand(request))));
    }

    @DeleteMapping("/costs/{costId}")
    public ResponseEntity<Void> deleteCost(@PathVariable final DepartmentCostId costId) {
        this.departmentCostPort.delete(costId);
        return ResponseEntity.noContent().build();
    }
}
