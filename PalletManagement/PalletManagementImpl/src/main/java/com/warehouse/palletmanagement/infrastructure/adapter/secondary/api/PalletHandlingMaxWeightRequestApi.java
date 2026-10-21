package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import java.math.BigDecimal;

public record PalletHandlingMaxWeightRequestApi(BigDecimal maxGrossWeight) {
}
