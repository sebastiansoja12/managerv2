package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;


public record ShipmentDto(ShipmentIdDto shipmentId, ShipmentStatusDto shipmentStatus, SenderDto sender,
		RecipientDto recipient, Boolean locked) {
}
