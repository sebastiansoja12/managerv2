package com.warehouse.departmentmap.domain.model;

import com.warehouse.commonassets.identificator.PalletPlacementId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.UserId;

import java.time.Instant;

public record PalletPlacement(PalletPlacementId placementId,
                              PalletTrackingNumber palletTrackingNumber,
                              UserId placedBy,
                              Instant placedAt) {
}
