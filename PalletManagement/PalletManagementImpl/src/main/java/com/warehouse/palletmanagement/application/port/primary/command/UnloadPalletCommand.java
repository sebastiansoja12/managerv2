package com.warehouse.palletmanagement.application.port.primary.command;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.List;

public record UnloadPalletCommand(PalletId palletId, List<ShipmentId> missingShipmentIds) {

    public UnloadPalletCommand {
        missingShipmentIds = missingShipmentIds == null ? List.of() : missingShipmentIds.stream().distinct().toList();
    }
}
