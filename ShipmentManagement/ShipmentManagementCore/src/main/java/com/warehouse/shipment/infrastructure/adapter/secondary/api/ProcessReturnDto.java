package com.warehouse.shipment.infrastructure.adapter.secondary.api;

public record ProcessReturnDto(ShipmentIdDto shipmentId, ReturnIdDto returnId, String processStatus) { }
