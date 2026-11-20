package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

public record PalletUpdateRequestApi(String palletType,
                                     String handlingPriority,
                                     Boolean refrigerated,
                                     Boolean mixedDestinationsAllowed,
                                     Double length,
                                     Double width,
                                     Double height,
                                     String sealNumber) {
}
