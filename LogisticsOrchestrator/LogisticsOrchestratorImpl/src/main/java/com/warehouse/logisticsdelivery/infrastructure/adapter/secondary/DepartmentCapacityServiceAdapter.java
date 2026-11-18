package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentCapacityServicePort;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentCapacity;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DepartmentCapacityPolicyDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationsDto;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;

import java.util.List;
import java.util.Objects;

public class DepartmentCapacityServiceAdapter implements DepartmentCapacityServicePort {

    private static final String ACTIVE_STATUS = "ACTIVE";

    private static final DepartmentCapacityPolicyDto DEFAULT_CAPACITY_POLICY =
            new DepartmentCapacityPolicyDto(1000, 85, false, 0);

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    private final DepartmentApiService departmentApiService;

    public DepartmentCapacityServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService,
                                            final DepartmentApiService departmentApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
        this.departmentApiService = departmentApiService;
    }

    @Override
    public List<DepartmentCapacity> findDepartments() {
        final DepartmentConfigurationsDto configurations =
                this.operatorConfigurationApiService.getDepartmentConfigurations();
        return this.departmentApiService.getAllDepartments().stream()
                .filter(department -> department.departmentId() != null)
                .map(department -> toCapacity(department, configurations))
                .toList();
    }

    private DepartmentCapacity toCapacity(final DepartmentDto department,
                                          final DepartmentConfigurationsDto configurations) {
        final DepartmentId departmentId = new DepartmentId(department.departmentId());
        final DepartmentCapacityPolicyDto capacityPolicy = capacityPolicy(configurations == null ? null
                : configurations.forDepartment(departmentId));
        final boolean hasCoordinates = department.coordinates() != null;
        return new DepartmentCapacity(departmentId,
                Objects.equals(ACTIVE_STATUS, department.status()),
                hasCoordinates ? department.coordinates().latitude() : null,
                hasCoordinates ? department.coordinates().longitude() : null,
                capacityPolicy.defaultDailyCapacity(),
                capacityPolicy.capacityWarningThresholdPercent(),
                capacityPolicy.redirectOverflowToNeighbour());
    }

    private DepartmentCapacityPolicyDto capacityPolicy(final DepartmentConfigurationDto configuration) {
        return configuration == null || configuration.capacityPolicy() == null
                ? DEFAULT_CAPACITY_POLICY : configuration.capacityPolicy();
    }
}
