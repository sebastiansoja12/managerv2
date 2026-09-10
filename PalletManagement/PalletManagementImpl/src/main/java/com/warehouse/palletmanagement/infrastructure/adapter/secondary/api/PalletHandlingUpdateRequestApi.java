package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

public record PalletHandlingUpdateRequestApi(String palletType,
                                             String handlingPriority,
                                             Boolean refrigerated,
                                             Double length,
                                             Double width,
                                             Double height,
                                             String sealNumber) {
}
