package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ShipmentAuditLogType;

import java.util.List;
import java.util.Objects;

public record ShipmentAuditLog(ShipmentId shipmentId,
                               ShipmentAuditLogType type,
                               List<ShipmentFieldChange> changes) {

    public ShipmentAuditLog {
        Objects.requireNonNull(shipmentId, "Shipment ID cannot be null");
        Objects.requireNonNull(type, "Shipment audit log type cannot be null");
        changes = List.copyOf(Objects.requireNonNull(changes, "Shipment field changes cannot be null"));
    }

    public ShipmentAuditLog(final ShipmentId shipmentId, final ShipmentAuditLogType type) {
        this(shipmentId, type, List.of());
    }
}
