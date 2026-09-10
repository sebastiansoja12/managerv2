package com.warehouse.pallet.infrastructure.adapter.primary.dto;

import java.util.List;

public record PalletShipmentsRequestApi(List<Long> shipmentIds) {
}
