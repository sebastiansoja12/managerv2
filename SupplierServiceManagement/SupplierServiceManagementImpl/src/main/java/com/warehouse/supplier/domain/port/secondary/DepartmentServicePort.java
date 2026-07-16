package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;

public interface DepartmentServicePort {
    Result<Void, Void> validateDepartmentCode(final DepartmentCode departmentCode);
}
