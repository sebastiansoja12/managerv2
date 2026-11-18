package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record ShipmentRouteToolResponse(String trackingNumber,
                                        List<DepartmentToolResponse> departments,
                                        Double distanceInKilometers) {
}
