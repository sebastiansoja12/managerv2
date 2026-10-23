package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record ShipmentEventToolResponse(String timestamp,
                                        String status,
                                        String processType,
                                        String processId,
                                        String departmentCode,
                                        String username,
                                        String description) {
}
