package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.event.*;
import com.warehouse.department.domain.exception.ForbiddenDepartmentTypeException;
import com.warehouse.department.domain.registry.DomainRegistry;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.domain.vo.TaxId;

import java.time.Instant;

public class Department {

    private DepartmentCode departmentCode;

    private Address address;

    private TaxId taxId;

    private String telephoneNumber;

    private String openingHours;
    
    private String email;

    private Boolean active;

    private DepartmentType departmentType;

    private Department.Status status;

    private Instant createdAt;

    private Instant updatedAt;

    private UserId adminUserId;

    private UserId createdBy;

    private UserId lastModifiedBy;

    public Department() {
    }

    public Department(
            final DepartmentCode departmentCode,
            final Address address,
            final TaxId taxId,
            final String telephoneNumber,
            final String openingHours,
            final String email,
            final Boolean active,
            final DepartmentType departmentType,
            final Status status,
            final Instant createdAt,
            final Instant updatedAt,
            final UserId adminUserId,
            final UserId createdBy,
            final UserId lastModifiedBy
    ) {
        this.address = address;
        this.departmentCode = departmentCode;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.departmentType = departmentType;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminUserId = adminUserId;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
    }

    public Department(
            final DepartmentCode departmentCode,
            final String city,
            final String street,
            final String postalCode,
            final TaxId taxId,
            final String telephoneNumber,
            final String openingHours,
            final String email,
            final Boolean active,
            final CountryCode countryCode,
            final DepartmentType departmentType
    ) {
        this.address = new Address(city, street, postalCode, countryCode);
        this.departmentCode = departmentCode;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.departmentType = departmentType;
        this.status = Status.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.adminUserId = null;
        this.createdBy = DomainRegistry.authenticationService().currentUser();
        this.lastModifiedBy = null;
        DomainRegistry.eventPublisher().publishEvent(
                new DepartmentCreated(this.snapshot(), Instant.now())
        );
    }


    public DepartmentSnapshot snapshot() {
		return new DepartmentSnapshot(departmentCode, address, taxId, telephoneNumber, openingHours, email, active,
				departmentType, status, createdAt, updatedAt, adminUserId, createdBy, lastModifiedBy);
    }

    public enum Status {
        ACTIVE,
        INACTIVE,
        ARCHIVED,
        DELETED,
        SUSPENDED
    }

    public String getCity() {
        return address.city();
    }

    public String getStreet() {
        return address.street();
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getPostalCode() {
        return address.postalCode();
    }

    public TaxId getTaxId() {
        return taxId;
    }

    public void setTaxId(final TaxId taxId) {
        this.taxId = taxId;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

    public CountryCode getCountryCode() {
        return address.countryCode();
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(final DepartmentType departmentType) {
        this.departmentType = departmentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public UserId getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(final UserId lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(final UserId createdBy) {
        this.createdBy = createdBy;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public UserId getAdminUserId() {
        return adminUserId;
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public void changeAddress(final Address address) {
        this.address = address;
        markAsModified();
    }

    public void changeTaxId(final TaxId newTaxId) {
        this.taxId = newTaxId;
        markAsModified();
    }

    public void activate(final UserId modifiedBy) {
        if (this.status == Status.DELETED) {
            throw new IllegalStateException("Department cannot be activated because it is deleted");
        }
        this.status = Status.ACTIVE;
        this.active = true;
        this.lastModifiedBy = modifiedBy;
        markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new DepartmentActivated(this.snapshot(), Instant.now()));
    }

    public void deactivate(final UserId modifiedBy) {
        this.status = Status.INACTIVE;
        this.active = false;
        this.lastModifiedBy = modifiedBy;
        markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new DepartmentDeactivated(this.snapshot(), Instant.now()));
    }

    public void markAsArchived() {
        this.status = Status.ARCHIVED;
        this.lastModifiedBy = lastModifiedBy;
        markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new DepartmentArchived(this.snapshot(), Instant.now()));
    }

    public void markAsDeleted() {
        this.status = Status.DELETED;
        this.lastModifiedBy = lastModifiedBy;
        markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new DepartmentDeleted(this.snapshot(), Instant.now()));
    }

    public void markAsSuspended() {
        this.status = Status.SUSPENDED;
        this.lastModifiedBy = lastModifiedBy;
        markAsModified();
    }

    public void changeDepartmentType(final DepartmentType departmentType) {
        if (this.departmentType == DepartmentType.HEADQUARTERS) {
            throw new ForbiddenDepartmentTypeException("Department type HEADQUARTERS cannot be changed");
        } else if (this.departmentType == DepartmentType.REMOTE_OFFICE) {
            throw new ForbiddenDepartmentTypeException("Department type REMOTE_OFFICE cannot be changed");
        }
        this.departmentType = departmentType;
        markAsModified();
        DomainRegistry.publish(new DepartmentTypeChanged(this.snapshot(), Instant.now()));
    }

    public void changeAdminUserId(final UserId adminUserId) {
        if (!adminUserId.isAdmin()) {
            throw new ForbiddenDepartmentTypeException("Non admin user cannot be assigned as admin user for department");
        }
        this.adminUserId = adminUserId;
        markAsModified();
    }
}
