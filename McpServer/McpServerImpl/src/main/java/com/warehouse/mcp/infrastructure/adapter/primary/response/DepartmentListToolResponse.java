package com.warehouse.mcp.infrastructure.adapter.primary.response;

import java.util.List;

public record DepartmentListToolResponse(int totalDepartments, int returnedDepartments,
                                         List<DepartmentToolResponse> departments) {
}
