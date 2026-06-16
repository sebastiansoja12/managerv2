package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;

public interface DepartmentServicePort {
    Result<Void, Void> validateDepartmentCode(final DepartmentCode departmentCode);

    DepartmentId getDepartmentId(final DepartmentCode departmentCode);

    DepartmentCode getDepartmentCode(final DepartmentId departmentId);
}
