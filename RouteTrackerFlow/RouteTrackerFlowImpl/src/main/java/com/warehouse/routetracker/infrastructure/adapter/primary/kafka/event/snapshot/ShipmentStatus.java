package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

public enum ShipmentStatus {
    PLANNED,
    CREATED,
    PREPARED,
    ACCEPTED,
    REROUTE,
    SENT,
    DELIVERY,
    RETURN,
    REDIRECT,
    CANCELED
}
