package com.warehouse.mcp.application.port.primary.result;

import java.util.List;

public record DeliveryList(List<DeliveryOverview> deliveries) {
}
