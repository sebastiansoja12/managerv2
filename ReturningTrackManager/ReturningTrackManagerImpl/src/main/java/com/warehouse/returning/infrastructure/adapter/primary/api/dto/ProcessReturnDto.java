package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

public record ProcessReturnDto(ShipmentIdDto shipmentId, ShipmentIdDto returnShipmentId, ReturnIdDto returnId,
                               String processStatus) { }
