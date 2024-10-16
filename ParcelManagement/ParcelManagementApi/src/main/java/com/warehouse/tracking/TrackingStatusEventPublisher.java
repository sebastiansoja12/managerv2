package com.warehouse.tracking;

public interface TrackingStatusEventPublisher {
    void send(final ShipmentStatusChanged shipmentStatusChanged);
}