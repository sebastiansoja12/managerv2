package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ChangeShipmentTypeRequest(ShipmentId shipmentId, ShipmentType shipmentType) {
}
