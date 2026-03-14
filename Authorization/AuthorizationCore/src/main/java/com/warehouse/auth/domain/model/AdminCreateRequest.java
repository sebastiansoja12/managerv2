package com.warehouse.auth.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;

public class AdminCreateRequest {
    private DepartmentCode departmentCode;
    private String email;
    private String telephoneNumber;
    private String language;

    public AdminCreateRequest(final DepartmentCode departmentCode, final String email, final String telephoneNumber,
                              final String language) {
        this.departmentCode = departmentCode;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.language = language;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }
}
