package com.warehouse.palletmanagement.infrastructure.adapter.primary.dto;

import java.util.List;

public record PalletUnloadRequestApi(List<Long> missingShipmentIds) {
}
