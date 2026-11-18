package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.api.dto.DepartmentConfigurationDto;
import com.warehouse.supplier.domain.port.secondary.CourierLimitServicePort;

public class CourierLimitServiceAdapter implements CourierLimitServicePort {

    private final OperatorConfigurationApiService operatorConfigurationApiService;

    public CourierLimitServiceAdapter(final OperatorConfigurationApiService operatorConfigurationApiService) {
        this.operatorConfigurationApiService = operatorConfigurationApiService;
    }

    @Override
    public int maxCouriersPerDepartment(final DepartmentId departmentId) {
        final DepartmentConfigurationDto configuration =
                this.operatorConfigurationApiService.getDepartmentConfiguration(departmentId);
        if (configuration == null || configuration.capacityPolicy() == null) {
            return 0;
        }
        return configuration.capacityPolicy().maxCouriersPerDepartment();
    }
}
