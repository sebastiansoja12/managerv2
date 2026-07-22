package com.warehouse.returning.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {
    boolean exists(final DepartmentId departmentId);
    String getCode(final DepartmentId departmentId);
}
