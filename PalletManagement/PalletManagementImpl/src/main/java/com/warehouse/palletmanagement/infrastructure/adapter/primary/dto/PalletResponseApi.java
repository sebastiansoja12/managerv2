package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PalletResponseApi(String palletId,
                                String trackingNumber,
                                String palletStatus,
                                String storageStatus,
                                List<Long> shipmentIds,
                                boolean empty,
                                boolean loadable,
                                String loadBlockReason,
                                BigDecimal grossWeight,
                                String grossWeightUnit,
                                BigDecimal maxGrossWeight,
                                String maxGrossWeightUnit,
                                Long originDepartmentId,
                                Long destinationDepartmentId,
                                String sealNumber,
                                Instant created,
                                Instant modified,
                                String palletType,
                                String handlingPriority,
                                boolean refrigerated,
                                PalletDimensionResponseApi dimension,
                                PalletCarrierResponseApi carrier) {
}
