package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.util.List;

public record CreatePalletRequestApi(List<Long> shipmentIds) {
}
