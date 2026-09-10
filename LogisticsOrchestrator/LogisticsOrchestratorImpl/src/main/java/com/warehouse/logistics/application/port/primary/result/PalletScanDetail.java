package com.warehouse.logistics.application.port.primary.result;

import com.warehouse.commonassets.identificator.ShipmentId;

public record PalletScanDetail(ShipmentId shipmentId, boolean success, String shipmentStatus, String errorMessage) {
}
