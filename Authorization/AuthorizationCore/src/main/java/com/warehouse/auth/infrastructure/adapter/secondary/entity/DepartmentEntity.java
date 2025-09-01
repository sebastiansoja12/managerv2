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

    @Column(name = "active", nullable = false)
    private boolean active;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final boolean active, final String departmentCode) {
        this.active = active;
        this.departmentCode = departmentCode;
    }

    public boolean isActive() {
        return active;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }
}
