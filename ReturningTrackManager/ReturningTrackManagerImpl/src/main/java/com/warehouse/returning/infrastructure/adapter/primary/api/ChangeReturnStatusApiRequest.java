package com.warehouse.returning.infrastructure.adapter.primary.api;

import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ShipmentIdDto;

public record ChangeReturnStatusApiRequest(ShipmentIdDto shipmentId, String returnStatus) {

    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
