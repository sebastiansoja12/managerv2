package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public record CreatePalletDeliveryCommand(String palletId, List<ShipmentId> shipmentIds, UserId userId) {
}
