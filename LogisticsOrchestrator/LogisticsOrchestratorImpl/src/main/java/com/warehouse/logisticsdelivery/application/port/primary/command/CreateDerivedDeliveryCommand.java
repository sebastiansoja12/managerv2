package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

public record CreateDerivedDeliveryCommand(ShipmentId shipmentId,
                                           Boolean signatureRequired,
                                           UserId userId) {
}
