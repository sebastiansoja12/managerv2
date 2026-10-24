package com.warehouse.mcp.application.port.primary.result;

import java.time.LocalDateTime;
import java.util.List;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mcp.domain.vo.MaskedShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentEvent;

public record ShipmentOverview(ShipmentId shipmentId,
                               String trackingNumber,
                               ShipmentStatus status,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               MaskedShipmentParty sender,
                               MaskedShipmentParty recipient,
                               int totalEvents,
                               List<ShipmentEvent> latestEvents) {
}
