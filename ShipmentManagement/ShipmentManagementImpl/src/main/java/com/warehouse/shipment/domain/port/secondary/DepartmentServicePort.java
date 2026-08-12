package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {

	DepartmentCode getDepartmentCode(final DepartmentId departmentId);
}
