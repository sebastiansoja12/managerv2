package com.warehouse.mcp.domain.vo;

import java.util.List;

public record ShipmentRoute(List<DepartmentEntry> departments, Double distanceInKilometers) {
}
