package com.warehouse.department.api.dto;

public record DepartmentDirectoryEntryDto(
        DepartmentIdDto departmentId,
        DepartmentCodeDto departmentCode,
        DepartmentTypeDto departmentType,
        DepartmentStatusDto status,
        CoordinatesDto coordinates) {
}
