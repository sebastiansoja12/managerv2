package com.warehouse.logistics.application.port.primary.result;

import java.util.List;

public record ShipmentAcceptResult(List<ShipmentAcceptDetail> details) {

    public boolean allAccepted() {
        return details.stream().allMatch(ShipmentAcceptDetail::accepted);
    }

    public String failureDescription() {
        if (allAccepted()) {
            return null;
        }
        return details.stream()
                .filter(detail -> !detail.accepted())
                .map(ShipmentAcceptDetail::errorMessage)
                .filter(message -> message != null && !message.isBlank())
                .findFirst()
                .orElse("Accept failed for at least one shipment");
    }
}
