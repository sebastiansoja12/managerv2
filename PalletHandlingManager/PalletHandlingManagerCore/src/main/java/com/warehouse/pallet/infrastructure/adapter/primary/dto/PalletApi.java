package com.warehouse.pallet.infrastructure.adapter.primary.dto;

import java.time.Instant;
import java.util.List;

public record PalletApi(String palletId,
                        String trackingNumber,
                        Long operatorId,
                        String palletStatus,
                        String storageStatus,
                        List<Long> shipmentIds,
                        boolean empty,
                        boolean loadable,
                        String loadBlockReason,
                        WeightApi grossWeight,
                        WeightApi maxGrossWeight,
                        Long originDepartmentId,
                        Long destinationDepartmentId,
                        String sealNumber,
                        Instant created,
                        Instant modified,
                        String palletType,
                        String handlingPriority,
                        boolean refrigerated,
                        DimensionApi dimension) {
}
