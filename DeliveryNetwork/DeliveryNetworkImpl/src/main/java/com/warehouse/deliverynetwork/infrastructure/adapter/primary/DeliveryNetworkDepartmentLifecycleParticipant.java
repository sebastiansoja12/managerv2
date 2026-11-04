package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.organisationstructure.department.api.DepartmentLifecycleParticipant;

import java.util.Optional;

public class DeliveryNetworkDepartmentLifecycleParticipant implements DepartmentLifecycleParticipant {

    private static final String MISSING_SORTING_CONNECTION = "no direct connection to an active sorting facility";

    private final DeliveryNetworkPort deliveryNetworkPort;

    public DeliveryNetworkDepartmentLifecycleParticipant(final DeliveryNetworkPort deliveryNetworkPort) {
        this.deliveryNetworkPort = deliveryNetworkPort;
    }

    @Override
    public void departmentCreated(final DepartmentId departmentId) {
        this.deliveryNetworkPort.connectDepartmentToSortingFacilities(departmentId);
    }

    @Override
    public Optional<String> activationBlocker(final DepartmentId departmentId) {
        return this.deliveryNetworkPort.canActivateDepartment(departmentId)
                ? Optional.empty()
                : Optional.of(MISSING_SORTING_CONNECTION);
    }
}
