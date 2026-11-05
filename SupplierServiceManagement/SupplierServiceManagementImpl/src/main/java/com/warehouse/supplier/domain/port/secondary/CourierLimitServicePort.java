package com.warehouse.supplier.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public interface CourierLimitServicePort {
    int maxCouriersPerDepartment(final DepartmentId departmentId);
}
