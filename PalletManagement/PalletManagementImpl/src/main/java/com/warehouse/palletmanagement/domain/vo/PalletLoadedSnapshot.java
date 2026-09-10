package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.util.List;

public record PalletLoadedSnapshot(PalletId palletId, PalletTrackingNumber trackingNumber, List<ShipmentId> shipmentIds) {
}
