package com.warehouse.terminal.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.terminal.domain.model.Department;

public interface DepartmentServicePort {
    Department getDepartment(final DepartmentCode departmentCode);
}
