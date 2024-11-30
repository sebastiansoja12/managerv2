package com.warehouse.delivery.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.delivery.domain.vo.Department;

public interface DepartmentRepository {
    Department findByCode(final DepartmentCode departmentCode);
}
