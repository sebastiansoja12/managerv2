package com.warehouse.tracking.infrastructure.adapter.primary.api;

public interface TrackingStatusEventPublisher {
    void send(final ShipmentStatusChanged shipmentStatusChanged);
}