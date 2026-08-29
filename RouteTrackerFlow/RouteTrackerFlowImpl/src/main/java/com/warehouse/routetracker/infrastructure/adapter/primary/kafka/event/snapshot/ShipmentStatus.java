package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

public enum ShipmentStatus {
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
