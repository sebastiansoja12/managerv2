package com.warehouse.supplier.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;

public interface CourierLimitServicePort {
    int maxCouriersPerDepartment(final DepartmentId departmentId);
}
