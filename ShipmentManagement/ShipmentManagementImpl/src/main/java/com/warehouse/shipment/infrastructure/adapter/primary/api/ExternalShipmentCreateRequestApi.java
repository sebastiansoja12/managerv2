package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ExternalShipmentCreateRequestApi(Long operatorId, ShipmentCreateRequestApi shipment) {
}
