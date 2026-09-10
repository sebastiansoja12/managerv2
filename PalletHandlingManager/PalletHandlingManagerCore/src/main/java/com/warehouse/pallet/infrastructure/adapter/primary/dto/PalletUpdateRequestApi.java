package com.warehouse.pallet.infrastructure.adapter.primary.dto;

public record PalletUpdateRequestApi(String palletType,
                                     String handlingPriority,
                                     Boolean refrigerated,
                                     Double length,
                                     Double width,
                                     Double height,
                                     String sealNumber) {
}
