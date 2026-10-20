package com.warehouse.logistics.application.port.primary.result;

import java.util.List;

public record ShipmentCollectResult(List<ShipmentCollectDetail> details) {

    public boolean allCollected() {
        return details.stream().allMatch(ShipmentCollectDetail::collected);
    }
}
