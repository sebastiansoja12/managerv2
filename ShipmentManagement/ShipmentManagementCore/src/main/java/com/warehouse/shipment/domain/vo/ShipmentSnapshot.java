package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentSnapshot(ShipmentId shipmentId, Sender sender, Recipient recipient, ShipmentStatus shipmentStatus) {
}
