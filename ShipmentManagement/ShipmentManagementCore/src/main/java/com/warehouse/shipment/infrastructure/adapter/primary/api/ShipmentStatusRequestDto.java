package com.warehouse.shipment.infrastructure.adapter.primary.api;

import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentStatusDto;

public record ShipmentStatusRequestDto(ShipmentIdDto shipmentId, ShipmentStatusDto shipmentStatus) {
}
