package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.event.domain.model.DomainEvent;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.time.Instant;
import java.util.Objects;

public class ShipmentDocumentAdded implements DomainEvent {

    private final ShipmentId shipmentId;

    private final String filename;

    private final Instant timestamp;

    public ShipmentDocumentAdded(final ShipmentId shipmentId, final String filename, final Instant timestamp) {
        this.shipmentId = Objects.requireNonNull(shipmentId, "Shipment ID cannot be null");
        this.filename = Objects.requireNonNull(filename, "Filename cannot be null");
        this.timestamp = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }
}
