package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {
    Boolean departmentExists(final DepartmentCode departmentCode);

    DepartmentId getDepartmentId(final DepartmentCode departmentCode);
}
