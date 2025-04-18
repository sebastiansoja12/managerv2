package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentCreateRequestDto(PersonDto sender, PersonDto recipient, ShipmentSizeDto shipmentSize,
		MoneyDto price, DangerousGoodDto dangerousGood) {
}
