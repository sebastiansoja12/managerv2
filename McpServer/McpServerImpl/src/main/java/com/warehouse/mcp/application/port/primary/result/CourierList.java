package com.warehouse.mcp.application.port.primary.result;

import java.util.List;

import com.warehouse.mcp.domain.vo.Courier;

public record CourierList(int totalCouriers, List<Courier> couriers) {
}
