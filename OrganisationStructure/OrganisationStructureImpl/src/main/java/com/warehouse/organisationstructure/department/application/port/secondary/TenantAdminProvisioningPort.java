package com.warehouse.organisationstructure.department.application.port.secondary;

import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;

public interface TenantAdminProvisioningPort {
    void createInitialAdminUser(final DepartmentSnapshot snapshot);
}
