package com.warehouse.organisationstructure.deliveryarea.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaBoundary;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaName;

public record CreateDeliveryAreaCommand(DepartmentId departmentId, DeliveryAreaName name, DeliveryAreaBoundary boundary) {
}
