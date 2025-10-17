package com.warehouse.auth.infrastructure.adapter.primary.event;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class AdminUserCommand {
    private final DepartmentCode departmentCode;
    private final String telephoneNumber;
    private final String email;

    public AdminUserCommand(final DepartmentCode departmentCode, final String telephoneNumber, final String email) {
        this.departmentCode = departmentCode;
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
}
