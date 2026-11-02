package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record CourierListToolResponse(int totalCouriers, int returnedCouriers, List<CourierToolResponse> couriers) {
}
