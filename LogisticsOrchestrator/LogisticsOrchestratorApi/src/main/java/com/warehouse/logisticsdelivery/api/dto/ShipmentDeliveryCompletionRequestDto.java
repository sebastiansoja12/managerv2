package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentDeliveryCompletionRequestDto(ShipmentId shipmentId, boolean recipientSigned) {
}
