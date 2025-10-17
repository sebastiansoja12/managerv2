package com.warehouse.department.domain.model;

import java.util.List;

public class DepartmentCreateRequest {
    private List<DepartmentCreate> departments;

    public DepartmentCreateRequest(final List<DepartmentCreate> departments) {
        this.departments = departments;
    }

    public List<DepartmentCreate> getDepartments() {
        return departments;
    }

    public void setDepartments(final List<DepartmentCreate> departments) {
        this.departments = departments;
    }
}
