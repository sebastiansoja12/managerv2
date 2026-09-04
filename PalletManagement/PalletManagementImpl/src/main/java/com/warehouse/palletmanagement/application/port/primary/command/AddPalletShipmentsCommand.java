package com.warehouse.palletmanagement.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.PalletId;

import java.util.List;

public record AddPalletShipmentsCommand(PalletId palletId, List<ShipmentId> shipmentIds) {

    public AddPalletShipmentsCommand {
        if (shipmentIds == null || shipmentIds.isEmpty()) {
            throw new IllegalArgumentException("At least one shipment is required");
        }
        shipmentIds = shipmentIds.stream().distinct().toList();
    }
}
