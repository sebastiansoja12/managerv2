package com.warehouse.shipment.domain.enumeration;

public enum ShipmentAuditLogType {
    CREATED,
    SENDER_CHANGED,
    RECIPIENT_CHANGED,
    REROUTED,
    TYPE_CHANGED,
    STATUS_CHANGED,
    CANCELED,
    LOCKED,
    SENT,
    COLLECTED,
    DELIVERED,
    REDIRECTED,
    RETURNED,
    RETURN_COMPLETED,
    NOTE_ADDED,
    DOCUMENT_ADDED
}
