package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

public enum ShipmentStatus {
    PLANNED,
    CREATED,
    PREPARED,
    ACCEPTED,
    COLLECTED,
    REROUTE,
    SENT,
    HANDOVER,
    DELIVERY,
    RETURN,
    REDIRECT,
    CANCELED,
    LOST
}
