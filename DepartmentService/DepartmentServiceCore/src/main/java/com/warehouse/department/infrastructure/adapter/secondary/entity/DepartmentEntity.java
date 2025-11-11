package com.warehouse.department.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import jakarta.persistence.*;

import java.time.Instant;


@Entity(name = "department.DepartmentEntity")
@Table(name = "department")
public class DepartmentEntity {

    @EmbeddedId
    @Column(name = "department_code", nullable = false, unique = true)
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "city", nullable = false)),
            @AttributeOverride(name = "street", column = @Column(name = "street", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "postal_code", nullable = false))
    })
    private DepartmentAddress departmentAddress;

    @Column(name = "nip", nullable = false)
    private String nip;

    @Column(name = "telephone_number", nullable = false)
    private String telephoneNumber;

    @Column(name = "opening_hours", nullable = false)
    private String openingHours;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "department_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final DepartmentCode departmentCode,
                            final DepartmentAddress departmentAddress,
                            final String nip,
                            final String telephoneNumber,
                            final String openingHours,
                            final String email,
                            final Boolean active,
                            final DepartmentType departmentType,
                            final Status status,
                            final Instant createdAt,
                            final Instant updatedAt) {
        this.departmentCode = departmentCode;
        this.departmentAddress = departmentAddress;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.departmentType = departmentType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Boolean isActive() {
        return active;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getNip() {
        return nip;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public DepartmentAddress getDepartmentAddress() {
        return departmentAddress;
    }

    public Status getStatus() {
        return status;
    }

    public enum DepartmentType {
        HEADQUARTERS,
        BRANCH,
        WAREHOUSE,
        SALES_OFFICE,
        SERVICE_CENTER,
        DISTRIBUTION,
        SORTING_FACILITY,
        REMOTE_OFFICE
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        ARCHIVED,
        DELETED,
        SUSPENDED
    }
}
