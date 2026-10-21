package com.warehouse.palletmanagement.domain.enumeration;

public enum PalletHistoryEventType {
    CREATED,
    SHIPMENTS_ADDED,
    SHIPMENT_REMOVED,
    GROSS_WEIGHT_CHANGED,
    MAX_WEIGHT_SET,
    DEPARTMENTS_CHANGED,
    PALLET_UPDATED,
    CARRIER_ASSIGNED,
    STATUS_CHANGED,
    LOADED,
    CANCELLED,
    UNKNOWN
}
