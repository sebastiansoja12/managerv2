package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentLifecycleParticipant;
import com.warehouse.supplier.domain.port.primary.SupplyPort;

import java.util.Optional;

public class SupplierDepartmentLifecycleParticipant implements DepartmentLifecycleParticipant {

    private final SupplyPort supplyPort;

    public SupplierDepartmentLifecycleParticipant(final SupplyPort supplyPort) {
        this.supplyPort = supplyPort;
    }

    @Override
    public Optional<String> activeWork(final DepartmentId departmentId) {
        final long assignedCouriers = this.supplyPort.countAssignedCouriers(departmentId);
        return assignedCouriers == 0 ? Optional.empty() : Optional.of(assignedCouriers + " assigned couriers");
    }

    @Override
    public void transferWork(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
        this.supplyPort.transferCouriers(sourceDepartmentId, targetDepartmentId);
    }
}
