package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {
    boolean departmentExists(final DepartmentId departmentId);
}
