package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.DeliveryId;

import java.time.LocalDateTime;

public record DeliveryAssignmentCursor(LocalDateTime eventTime, DeliveryId deliveryId) {
}
