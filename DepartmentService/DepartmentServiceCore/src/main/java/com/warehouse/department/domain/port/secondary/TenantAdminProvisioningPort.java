package com.warehouse.department.domain.port.secondary;

import com.warehouse.department.domain.vo.DepartmentSnapshot;

public interface TenantAdminProvisioningPort {
    void createInitialAdminUser(final DepartmentSnapshot snapshot);
}
