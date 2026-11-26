package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentConfigServicePort;
import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationsDto;
import com.warehouse.organisationstructure.api.dto.DepartmentCourierAssignmentPolicyDto;
import com.warehouse.organisationstructure.api.dto.RegionalConfigurationDto;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DepartmentAssignmentConfigServiceAdapter implements DepartmentAssignmentConfigServicePort {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    private final DepartmentApiService departmentApiService;

    private final ZoneId defaultZone;

    public DepartmentAssignmentConfigServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService,
                                                    final DepartmentApiService departmentApiService,
                                                    final ZoneId defaultZone) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.departmentApiService = departmentApiService;
        this.defaultZone = defaultZone;
    }

    @Override
    public List<DepartmentAssignmentConfig> findConfigs() {
        final DepartmentConfigurationsDto configurations =
                this.operatorConfigurationApiService.getDepartmentConfigurations();
        final ZoneId operatorZone = zone(Optional.ofNullable(
                this.operatorConfigurationApiService.getCurrentRegionalConfiguration())
                .map(RegionalConfigurationDto::timeZone)
                .orElse(null), this.defaultZone);
        return this.departmentApiService.getAllDepartments().stream()
                .filter(department -> department.departmentId() != null)
                .filter(department -> Objects.equals(ACTIVE_STATUS, department.status()))
                .map(DepartmentDto::departmentId)
                .map(DepartmentId::new)
                .flatMap(departmentId -> Optional.ofNullable(configurations.forDepartment(departmentId))
                        .map(DepartmentConfigurationDto::courierAssignmentPolicy)
                        .map(policy -> toConfig(departmentId, policy, operatorZone))
                        .stream())
                .toList();
    }

    private DepartmentAssignmentConfig toConfig(final DepartmentId departmentId,
                                                final DepartmentCourierAssignmentPolicyDto policy,
                                                final ZoneId operatorZone) {
        return new DepartmentAssignmentConfig(departmentId, policy.enabled(),
                policy.dateField() == null ? CourierAssignmentDateField.DESTINATION_ARRIVAL
                        : CourierAssignmentDateField.valueOf(policy.dateField().name()),
                policy.windowType() == null ? AssignmentWindowType.ROLLING
                        : AssignmentWindowType.valueOf(policy.windowType().name()),
                policy.windowStart(), policy.windowEnd(), zone(policy.timeZone(), operatorZone),
                Duration.ofMinutes(policy.processingDelayMinutes()), Duration.ofMinutes(policy.overlapMinutes()),
                Duration.ofHours(policy.maxWindowHours()), Duration.ofDays(policy.lookbackDays()),
                policy.batchSize());
    }

    private static ZoneId zone(final String timeZone, final ZoneId fallback) {
        if (timeZone == null || timeZone.isBlank()) {
            return fallback;
        }
        try {
            return ZoneId.of(timeZone);
        } catch (final DateTimeException exception) {
            return fallback;
        }
    }
}
