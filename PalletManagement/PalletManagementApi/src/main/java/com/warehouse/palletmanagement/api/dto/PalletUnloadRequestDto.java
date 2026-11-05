package com.warehouse.palletmanagement.api.dto;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;

public record PalletUnloadRequestDto(PalletTrackingNumber trackingNumber) {
}
