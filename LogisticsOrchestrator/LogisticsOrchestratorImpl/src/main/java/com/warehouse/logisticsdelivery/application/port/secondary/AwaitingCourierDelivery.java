package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;

public record AwaitingCourierDelivery(Delivery delivery, DeliveryAssignmentCursor position) {
}
