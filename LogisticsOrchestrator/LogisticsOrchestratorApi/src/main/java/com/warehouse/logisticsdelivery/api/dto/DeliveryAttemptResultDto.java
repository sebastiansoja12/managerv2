package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.ShipmentId;

public record DeliveryAttemptResultDto(DeliveryId deliveryId, ShipmentId shipmentId, DeliveryTargetDto target) {
}
