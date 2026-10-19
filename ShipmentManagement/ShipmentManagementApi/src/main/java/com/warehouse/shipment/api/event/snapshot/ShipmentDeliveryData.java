package com.warehouse.shipment.api.event.snapshot;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PickupPointId;

public record ShipmentDeliveryData(
        DepartmentId originDepartmentId,
        DepartmentId destinationDepartmentId,
        PickupMethod pickupMethod,
        DeliveryMethod deliveryMethod,
        PickupPointId pickupPointId,
        PickupPointId deliveryPickupPointId
) {
    public enum PickupMethod {
        DEPARTMENT,
        COURIER,
        PICKUP_POINT,
        LOCKER
    }

    public enum DeliveryMethod {
        COURIER,
        PICKUP_POINT,
        LOCKER
    }
}
