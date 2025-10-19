package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class AdminCreateRequest {
    private DepartmentCode departmentCode;
    private String email;
    private String telephoneNumber;

    public AdminCreateRequest(final DepartmentCode departmentCode, final String email, final String telephoneNumber) {
        this.departmentCode = departmentCode;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(final String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
