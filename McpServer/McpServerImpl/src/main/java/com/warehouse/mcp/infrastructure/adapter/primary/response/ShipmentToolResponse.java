package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record ShipmentToolResponse(Long shipmentId,
                                   String trackingNumber,
                                   String status,
                                   String createdAt,
                                   String updatedAt,
                                   ShipmentPartyToolResponse sender,
                                   ShipmentPartyToolResponse recipient,
                                   int totalEvents,
                                   List<ShipmentEventToolResponse> latestEvents) {
}
