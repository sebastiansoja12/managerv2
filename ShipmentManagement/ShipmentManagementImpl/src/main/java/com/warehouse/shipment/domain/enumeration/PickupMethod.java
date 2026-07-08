package com.warehouse.shipment.domain.enumeration;

public enum PickupMethod {
    DEPARTMENT,
    COURIER,
    PICKUP_POINT,
    LOCKER;

    public boolean isPickupPointBased() {
        return this == PICKUP_POINT || this == LOCKER;
    }
}
