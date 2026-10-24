package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record ProcessStepToolResponse(String createdAt,
                                      String processType,
                                      String serviceType,
                                      String sourceService,
                                      String targetService,
                                      String departmentCode,
                                      String faultDescription) {
}
