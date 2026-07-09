package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

public record ProcessReturnDto(ShipmentIdDto shipmentId, ReturnIdDto returnId, String processStatus) { }
