package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record ProcessToolResponse(String processId,
                                  String status,
                                  String createdAt,
                                  String modifiedAt,
                                  String faultDescription,
                                  String deviceId,
                                  String departmentCode,
                                  Long userId,
                                  List<ProcessStepToolResponse> steps) {
}
