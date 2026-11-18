package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record DeliveryListToolResponse(int returnedDeliveries, List<DeliveryToolResponse> deliveries) {
}
