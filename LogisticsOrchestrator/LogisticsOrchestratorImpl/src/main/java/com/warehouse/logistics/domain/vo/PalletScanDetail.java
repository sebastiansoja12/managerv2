package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.identificator.ShipmentId;

public record PalletScanDetail(ShipmentId shipmentId, boolean success, String shipmentStatus, String errorMessage) {
}
