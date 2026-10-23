package com.warehouse.mcp.infrastructure.adapter.primary.response;

public record CurrentUserToolResponse(String username,
                                      String fullName,
                                      String role,
                                      String departmentCode,
                                      Long operatorId) {
}
