package com.warehouse.logistics.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.logistics.domain.vo.Department;

public interface DepartmentRepository {
    Department findByCode(final DepartmentCode departmentCode);
}
