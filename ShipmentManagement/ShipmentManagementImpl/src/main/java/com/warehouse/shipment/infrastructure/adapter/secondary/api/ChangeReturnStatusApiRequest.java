package com.warehouse.shipment.infrastructure.adapter.secondary.api;

public record ChangeReturnStatusApiRequest(ShipmentIdDto shipmentId, String returnStatus) {
}
