package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public interface DepartmentNotifierServicePort {
    void notifyDepartmentWithAdminUser(final DepartmentCode departmentCode, final UserId userId);
}
