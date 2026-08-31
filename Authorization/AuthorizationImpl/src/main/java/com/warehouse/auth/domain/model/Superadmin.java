package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;

public class Superadmin extends OrganizationUser {

    @Override
    public Role getRole() {
        return Role.SUPERADMIN;
    }

    @Override
    public boolean canAccessDepartment(final DepartmentId departmentId) {
        return true;
    }

    @Override
    protected boolean canGrant(final AccountLevel level) {
        return getLevel().isAtLeast(level);
    }
}
