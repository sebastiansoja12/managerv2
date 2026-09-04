package com.warehouse.palletmanagement.application.port.primary.command;

import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.List;

public record CreatePalletCommand(List<ShipmentId> shipmentIds) {

    public CreatePalletCommand {
        if (shipmentIds == null || shipmentIds.isEmpty()) {
            throw new IllegalArgumentException("At least one shipment is required");
        }
        shipmentIds = shipmentIds.stream().distinct().toList();
    }
}
