package com.warehouse.returning.infrastructure.adapter.secondary.api;

import java.util.List;

public record RtmCreateResponse(List<CreatedReturnApi> processReturn) {

    public record CreatedReturnApi(ShipmentIdApi shipmentId, ReturnIdApi returnId, String processStatus) {
    }

    public record ShipmentIdApi(Long value) {
    }

    public record ReturnIdApi(Long value) {
    }
}
