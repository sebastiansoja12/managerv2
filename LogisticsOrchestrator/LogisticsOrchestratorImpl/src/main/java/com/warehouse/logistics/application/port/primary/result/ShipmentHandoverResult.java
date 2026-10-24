package com.warehouse.logistics.application.port.primary.result;

import java.util.List;

public record ShipmentHandoverResult(List<ShipmentHandoverDetail> details, boolean recipientSigned) {

    public boolean allHandedOver() {
        return details.stream().allMatch(ShipmentHandoverDetail::handedOver);
    }
}
