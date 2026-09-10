package com.warehouse.palletmanagement.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;

public record PalletScanShipmentResultDto(ShipmentId shipmentId,
                                          boolean success,
                                          String shipmentStatus,
                                          String errorMessage) {
}
