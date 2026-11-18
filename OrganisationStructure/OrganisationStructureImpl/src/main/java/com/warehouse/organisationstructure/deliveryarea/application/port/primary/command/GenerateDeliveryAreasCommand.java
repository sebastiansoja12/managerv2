package com.warehouse.organisationstructure.deliveryarea.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.util.List;

public record GenerateDeliveryAreasCommand(List<DepartmentId> departmentIds) {
}
