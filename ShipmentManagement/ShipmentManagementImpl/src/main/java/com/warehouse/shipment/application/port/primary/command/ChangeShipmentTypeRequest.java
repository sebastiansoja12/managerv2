package com.warehouse.shipment.application.port.primary.command;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ChangeShipmentTypeRequest(ShipmentId shipmentId, ShipmentType shipmentType) {
}
