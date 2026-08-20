package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentCreateRequestApi(PersonApi sender, PersonApi recipient, ShipmentSizeDto shipmentSize,
									   MoneyApi price, DangerousGoodApi dangerousGood, ShipmentPriorityDto shipmentPriority,
									   String issuerCountryCode, String receiverCountryCode) {
}
