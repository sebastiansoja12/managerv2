package com.warehouse.palletmanagement.api.event.snapshot;

import java.util.List;

public record PalletLoadingRevertedEventData(String palletId, String trackingNumber, List<Long> shipmentIds) {
}
