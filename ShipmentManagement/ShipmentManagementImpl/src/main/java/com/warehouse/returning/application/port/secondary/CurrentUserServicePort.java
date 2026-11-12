package com.warehouse.returning.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public interface CurrentUserServicePort {

    boolean canAccessDepartment(final DepartmentId departmentId);
}
