package com.warehouse.auth.infrastructure.adapter.primary.event;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.function.Consumer;

public class AdminUserCommand {
    private final DepartmentCode departmentCode;
    private final String telephoneNumber;
    private final String email;
    private final Consumer<UserId> adminCreatedId;

    public AdminUserCommand(final DepartmentCode departmentCode, final String telephoneNumber, final String email,
                            final Consumer<UserId> adminCreatedId) {
        this.departmentCode = departmentCode;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
        this.adminCreatedId = adminCreatedId;
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

    public Consumer<UserId> getAdminCreatedId() {
        return adminCreatedId;
    }
}
