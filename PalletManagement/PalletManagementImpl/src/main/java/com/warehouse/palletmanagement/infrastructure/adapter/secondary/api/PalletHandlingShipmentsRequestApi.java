package com.warehouse.palletmanagement.infrastructure.adapter.secondary.api;

import java.util.List;

public record PalletHandlingShipmentsRequestApi(List<Long> shipmentIds) {
}
