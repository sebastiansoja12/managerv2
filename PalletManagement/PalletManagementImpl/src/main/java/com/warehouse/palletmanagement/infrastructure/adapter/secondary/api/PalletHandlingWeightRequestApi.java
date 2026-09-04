package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import java.math.BigDecimal;

public record PalletHandlingWeightRequestApi(BigDecimal grossWeight, String grossWeightUnit) {
}
