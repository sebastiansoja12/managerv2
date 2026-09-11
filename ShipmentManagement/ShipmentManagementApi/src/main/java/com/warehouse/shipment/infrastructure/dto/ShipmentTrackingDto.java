package com.warehouse.shipment.infrastructure.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentTrackingDto(ShipmentId shipmentId,
                                  String trackingNumber,
                                  ShipmentStatus status,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt,
                                  ShipmentPartyDto sender,
                                  ShipmentPartyDto recipient,
                                  List<ShipmentTrackingEventDto> events) {
}
