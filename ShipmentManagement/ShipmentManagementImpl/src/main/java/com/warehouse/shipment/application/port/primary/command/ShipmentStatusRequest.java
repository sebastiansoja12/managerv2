package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import lombok.Builder;

@Builder
public record ShipmentStatusRequest(ShipmentId shipmentId, ShipmentStatus shipmentStatus) {
}
