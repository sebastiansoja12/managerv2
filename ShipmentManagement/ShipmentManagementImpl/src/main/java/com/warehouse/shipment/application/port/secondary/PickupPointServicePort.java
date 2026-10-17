package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;

import java.util.Optional;

public interface PickupPointServicePort {

    Optional<DepartmentId> findDepartmentId(final PickupPointId pickupPointId);
}
