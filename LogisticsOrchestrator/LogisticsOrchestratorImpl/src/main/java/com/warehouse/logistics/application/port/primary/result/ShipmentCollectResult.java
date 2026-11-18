package com.warehouse.logistics.application.port.primary.result;

import java.util.List;

public record ShipmentCollectResult(List<ShipmentCollectDetail> details) {

    public boolean allCollected() {
        return details.stream().allMatch(ShipmentCollectDetail::collected);
    }

    public String failureDescription() {
        if (allCollected()) {
            return null;
        }
        return details.stream()
                .filter(detail -> !detail.collected())
                .map(ShipmentCollectDetail::errorMessage)
                .filter(message -> message != null && !message.isBlank())
                .findFirst()
                .orElse("Collect failed for at least one shipment");
    }
}
