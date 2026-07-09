package com.warehouse.returning.domain.vo;

import com.warehouse.returning.infrastructure.adapter.primary.api.dto.ShipmentIdApi;

public record ShipmentId(Long value) {
    public static ShipmentId of(final ShipmentIdApi shipmentIdApi) {
        return new ShipmentId(shipmentIdApi.value());
    }
}
