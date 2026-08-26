package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public interface ShipmentEvent {

    ShipmentSnapshot getSnapshot();

    Instant getTimestamp();
}
