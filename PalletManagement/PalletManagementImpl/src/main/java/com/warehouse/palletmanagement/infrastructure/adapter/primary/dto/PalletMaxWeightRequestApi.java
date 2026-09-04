package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.math.BigDecimal;

public record PalletMaxWeightRequestApi(BigDecimal maxGrossWeight) {
}
