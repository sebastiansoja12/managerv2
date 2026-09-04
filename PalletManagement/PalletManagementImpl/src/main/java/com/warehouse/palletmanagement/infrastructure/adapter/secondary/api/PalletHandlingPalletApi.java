package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PalletHandlingPalletApi(String palletId,
                                      String trackingNumber,
                                      Long operatorId,
                                      String palletStatus,
                                      String storageStatus,
                                      List<Long> shipmentIds,
                                      boolean empty,
                                      boolean loadable,
                                      String loadBlockReason,
                                      PalletHandlingWeightApi grossWeight,
                                      PalletHandlingWeightApi maxGrossWeight,
                                      Long originDepartmentId,
                                      Long destinationDepartmentId,
                                      String sealNumber,
                                      Instant created,
                                      Instant modified,
                                      String palletType,
                                      String handlingPriority,
                                      boolean refrigerated,
                                      PalletHandlingDimensionApi dimension) {
}
