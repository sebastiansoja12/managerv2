package com.warehouse.mcp.domain.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public record ShipmentTracking(ShipmentId shipmentId,
                               String trackingNumber,
                               ShipmentStatus status,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               ShipmentParty sender,
                               ShipmentParty recipient,
                               List<ShipmentEvent> events) {
}
