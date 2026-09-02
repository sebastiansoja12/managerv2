package com.warehouse.deliverynetwork.domain.enumeration;

public enum DepartmentStatus {
    ACTIVE,
    INACTIVE,
    ARCHIVED,
    DELETED,
    SUSPENDED;

    public boolean participatesInDeliveryNetwork() {
        return this != ARCHIVED && this != DELETED;
    }
}
