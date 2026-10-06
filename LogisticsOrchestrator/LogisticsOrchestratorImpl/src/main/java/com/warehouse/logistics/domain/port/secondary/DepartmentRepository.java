package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentRepository {
    boolean existsByCode(final DepartmentCode departmentCode);

    DepartmentId findIdByCode(final DepartmentCode departmentCode);
}
