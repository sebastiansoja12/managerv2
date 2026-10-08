package com.warehouse.mcp.application.port.primary.result;

import java.util.List;

import com.warehouse.mcp.domain.vo.DepartmentEntry;

public record DepartmentList(int totalDepartments, List<DepartmentEntry> departments) {
}
