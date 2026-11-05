package com.warehouse.logistics.application.port.primary.result;

import java.util.List;

public record ShipmentHandoverResult(List<ShipmentHandoverDetail> details, boolean recipientSigned) {

    public boolean allHandedOver() {
        return details.stream().allMatch(ShipmentHandoverDetail::handedOver);
    }

    public String failureDescription() {
        if (allHandedOver()) {
            return null;
        }
        return details.stream()
                .filter(detail -> !detail.handedOver())
                .map(ShipmentHandoverDetail::errorMessage)
                .filter(message -> message != null && !message.isBlank())
                .findFirst()
                .orElse("Handover failed for at least one shipment");
    }
}
