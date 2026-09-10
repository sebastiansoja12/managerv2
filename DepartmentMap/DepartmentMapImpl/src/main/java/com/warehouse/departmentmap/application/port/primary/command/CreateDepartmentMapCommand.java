package com.warehouse.departmentmap.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;

public record CreateDepartmentMapCommand(DepartmentId departmentId,
                                         String name,
                                         double width,
                                         double height,
                                         double gridSize) {
}
