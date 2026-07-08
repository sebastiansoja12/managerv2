package com.warehouse.shipment.domain.enumeration;

public enum DeliveryMethod {
    COURIER, PICKUP_POINT, LOCKER;

    public boolean isPickupPointBased() {
        return this == PICKUP_POINT || this == LOCKER;
    }
}
