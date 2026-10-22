package com.warehouse.supplierconfiguration.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

public interface CurrentUserServicePort {
    UserId getCurrentUserId();
    boolean canAccessDepartment(final DepartmentId departmentId);
}
