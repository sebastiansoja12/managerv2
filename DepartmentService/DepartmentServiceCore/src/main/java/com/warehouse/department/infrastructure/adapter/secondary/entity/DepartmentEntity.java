package com.warehouse.department.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
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

    @Column(name = "tax_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "tax_id"))
    private TaxId taxId;

    @Column(name = "telephone_number", nullable = false)
    private String telephoneNumber;

    @Column(name = "opening_hours", nullable = false)
    private String openingHours;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

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

    @Column(name = "admin_user_id")
    @AttributeOverride(name = "value", column = @Column(name = "admin_user_id"))
    private UserId adminUserId;

    @Column(name = "created_by", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "created_by"))
    private UserId createdBy;

    @Column(name = "last_modified_by")
    @AttributeOverride(name = "value", column = @Column(name = "last_modified_by"))
    private UserId lastModifiedBy;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final DepartmentCode departmentCode,
                            final DepartmentAddress departmentAddress,
                            final TaxId taxId,
                            final String telephoneNumber,
                            final String openingHours,
                            final String email,
                            final DepartmentType departmentType,
                            final Status status,
                            final Instant createdAt,
                            final Instant updatedAt,
                            final UserId adminUserId,
                            final UserId createdBy,
                            final UserId lastModifiedBy) {
        this.departmentCode = departmentCode;
        this.departmentAddress = departmentAddress;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.departmentType = departmentType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminUserId = adminUserId;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public TaxId getTaxId() {
        return taxId;
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

    public UserId getAdminUserId() {
        return adminUserId;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public UserId getLastModifiedBy() {
        return lastModifiedBy;
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
