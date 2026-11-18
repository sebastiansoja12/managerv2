package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentWorkloadPort;
import com.warehouse.organisationstructure.department.api.DepartmentLifecycleParticipant;

import java.util.Optional;

public class DeliveryDepartmentLifecycleParticipant implements DepartmentLifecycleParticipant {

    private final DepartmentWorkloadPort departmentWorkloadPort;

    public DeliveryDepartmentLifecycleParticipant(final DepartmentWorkloadPort departmentWorkloadPort) {
        this.departmentWorkloadPort = departmentWorkloadPort;
    }

    @Override
    public Optional<String> activeWork(final DepartmentId departmentId) {
        final long activeDeliveries = this.departmentWorkloadPort.countActiveDeliveries(departmentId);
        return activeDeliveries == 0 ? Optional.empty() : Optional.of(activeDeliveries + " active deliveries");
    }

    @Override
    public void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
        this.departmentWorkloadPort.transferActiveDeliveries(sourceDepartmentId, targetDepartmentId);
    }
}
