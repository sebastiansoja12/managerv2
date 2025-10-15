package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.domain.port.secondary.TenantAdminProvisioningPort;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

public class TenantAdminProvisioningAdapter implements TenantAdminProvisioningPort {
    @Override
    public void createInitialAdminUser(final DepartmentSnapshot snapshot) {

    }
}
