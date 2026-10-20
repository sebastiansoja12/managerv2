package com.warehouse.logistics.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {
    boolean existsByCode(final DepartmentCode departmentCode);

    DepartmentId findIdByCode(final DepartmentCode departmentCode);
}
