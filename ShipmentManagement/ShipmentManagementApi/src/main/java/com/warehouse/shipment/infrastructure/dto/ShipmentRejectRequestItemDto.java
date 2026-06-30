package com.warehouse.shipment.infrastructure.dto;

public record ShipmentRejectRequestItemDto(Long shipmentId,
                                           String processType,
                                           String deliveryStatus,
                                           String shipmentStatus) {
}
