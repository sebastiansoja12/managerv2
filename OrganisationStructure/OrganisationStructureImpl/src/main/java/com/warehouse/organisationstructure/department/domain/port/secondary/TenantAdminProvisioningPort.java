package com.warehouse.organisationstructure.department.domain.port.secondary;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

public interface TenantAdminProvisioningPort {
    void createInitialAdminUser(final DepartmentSnapshot snapshot);
}
