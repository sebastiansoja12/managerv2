package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

import com.warehouse.deliveryreject.dto.DeliveryStatusDto;
import com.warehouse.deliveryreject.dto.ShipmentIdDto;

public record RejectTrackerRequestDto(ShipmentIdDto shipmentId, RejectReasonDto rejectReason, DeliveryStatusDto deliveryStatus) {
}
