package com.warehouse.auth.infrastructure.adapter.primary.event;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public class DepartmentUserChanged {
    private final DepartmentCode departmentCode;
    private final UserId userId;
    private final String telephoneNumber;
    private final String email;

    public DepartmentUserChanged(final DepartmentCode departmentCode, final UserId userId, final String telephoneNumber, final String email) {
        this.departmentCode = departmentCode;
        this.userId = userId;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public UserId getUserId() {
        return userId;
    }
}
