package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public record UnloadPalletDeliveryCommand(PalletId palletId, List<ShipmentId> shipmentIds,
                                          DepartmentId unloadDepartmentId, UserId userId) {
}
