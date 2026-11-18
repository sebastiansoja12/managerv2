package com.warehouse.palletmanagement.api.dto;

import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;

import java.util.List;

public record PalletUnloadResponseDto(PalletId palletId,
                                      PalletTrackingNumber trackingNumber,
                                      String palletStatus,
                                      boolean success,
                                      boolean alreadyUnloaded,
                                      String errorCode,
                                      String errorMessage,
                                      List<PalletScanShipmentResultDto> shipments) {
}
