package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

public record ShipmentId(Long value) {

    public Long getValue() {
        return value;
    }
}
