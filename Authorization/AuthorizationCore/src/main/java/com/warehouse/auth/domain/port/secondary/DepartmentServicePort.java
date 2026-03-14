package com.warehouse.auth.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;

public interface DepartmentServicePort {
    Boolean departmentExists(final DepartmentCode departmentCode);
}
