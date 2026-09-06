package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Table(name = "department")
@Entity(name = "user.DepartmentEntity")
public class DepartmentEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "department_id", nullable = false))
    private DepartmentId departmentId;

    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "status", nullable = false)
    private String status;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final DepartmentId departmentId, final String status, final String departmentCode) {
        this.departmentId = departmentId;
        this.status = status;
        this.departmentCode = departmentCode;
    }

    public boolean isActive() {
        return status.equals("ACTIVE");
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }
}
