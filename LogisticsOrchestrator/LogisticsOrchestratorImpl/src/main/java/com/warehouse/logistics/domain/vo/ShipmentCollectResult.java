package com.warehouse.logistics.domain.vo;

import java.util.List;

public record ShipmentCollectResult(List<ShipmentCollectDetail> details) {

    public boolean allCollected() {
        return details.stream().allMatch(ShipmentCollectDetail::collected);
    }
}
