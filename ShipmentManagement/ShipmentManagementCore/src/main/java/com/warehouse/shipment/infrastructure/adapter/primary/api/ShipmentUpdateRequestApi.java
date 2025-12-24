package com.warehouse.shipment.infrastructure.adapter.primary.api;

public record ShipmentUpdateRequestApi(ShipmentIdDto shipmentId, PersonApi sender, PersonApi recipient,
		String destination,
		ShipmentSizeDto shipmentSize, MoneyApi price, DangerousGoodApi dangerousGood,
		ShipmentPriorityDto shipmentPriority, ShipmentStatusDto shipmentStatus, String issuerCountryCode,
		String receiverCountryCode, ShipmentConfigurationApi shipmentConfiguration) {
}
