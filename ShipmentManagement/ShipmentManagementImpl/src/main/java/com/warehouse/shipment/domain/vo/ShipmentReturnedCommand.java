package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.enumeration.ReasonCode;

public record ShipmentReturnedCommand(ShipmentId shipmentId, ReasonCode reasonCode, String reason) {
}
