package com.warehouse.organisationstructure.deliveryarea.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDraft;

import java.util.List;

public record ReplaceDepartmentDeliveryAreasCommand(DepartmentId departmentId, List<DeliveryAreaDraft> areas) {
}
