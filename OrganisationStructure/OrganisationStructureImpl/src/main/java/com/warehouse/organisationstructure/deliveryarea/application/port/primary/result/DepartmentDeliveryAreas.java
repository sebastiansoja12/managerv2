package com.warehouse.organisationstructure.deliveryarea.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;

import java.util.List;

public record DepartmentDeliveryAreas(DepartmentId departmentId, DepartmentCode departmentCode,
                                      List<DeliveryArea> areas) {
}
