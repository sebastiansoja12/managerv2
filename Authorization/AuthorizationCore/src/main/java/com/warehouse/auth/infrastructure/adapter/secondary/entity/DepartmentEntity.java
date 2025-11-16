package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "department")
@Entity(name = "user.DepartmentEntity")
public class DepartmentEntity {

    @Id
    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "status", nullable = false)
    private String status;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final String status, final String departmentCode) {
        this.status = status;
        this.departmentCode = departmentCode;
    }

    public boolean isActive() {
        return status.equals("ACTIVE");
    }

    public String getDepartmentCode() {
        return departmentCode;
    }
}
