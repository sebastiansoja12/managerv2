package com.warehouse.incident.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

import java.util.Objects;

public record IncidentShipment(ShipmentId shipmentId, TrackingNumber trackingNumber) {

    public IncidentShipment {
        Objects.requireNonNull(shipmentId, "Incident shipment ID cannot be null");
    }
}
