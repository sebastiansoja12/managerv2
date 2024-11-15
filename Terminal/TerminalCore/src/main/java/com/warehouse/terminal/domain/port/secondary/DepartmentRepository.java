package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.terminal.domain.model.Department;

public interface DepartmentRepository {
    boolean existsByDepartmentCode(final DepartmentCode departmentCode);
    Department findByDepartmentCode(final DepartmentCode departmentCode);
}
