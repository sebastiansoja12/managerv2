package com.warehouse.organisationstructure.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.event.DepartmentTypeChanged;
import com.warehouse.organisationstructure.department.domain.exception.DepartmentStatusChangeForbiddenException;
import com.warehouse.organisationstructure.department.domain.exception.ForbiddenDepartmentTypeException;
import com.warehouse.organisationstructure.department.domain.exception.WrongUserAssignedException;
import com.warehouse.organisationstructure.department.domain.registry.DomainRegistry;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.Coordinates;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentSnapshot;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusChange;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;
import com.warehouse.organisationstructure.department.domain.vo.TaxId;
import org.apache.commons.lang3.Validate;

import java.time.Instant;

public class Department extends BelongsToOperator {

    private DepartmentId departmentId;

    private DepartmentCode departmentCode;

    private Address address;

    private TaxId taxId;

    private String telephoneNumber;

    private String openingHours;
    
    private String email;

    private DepartmentType departmentType;

    private Department.Status status;

    private Coordinates coordinates;

    private Instant createdAt;

    private Instant updatedAt;

    private UserId adminUserId;

    private UserId createdBy;

    private UserId lastModifiedBy;

    private String statusChangeReason;

    private Instant statusChangedAt;

    public Department() {
    }

    public Department(
            final DepartmentId departmentId,
            final DepartmentCode departmentCode,
            final Address address,
            final TaxId taxId,
            final String telephoneNumber,
            final String openingHours,
            final String email,
            final DepartmentType departmentType,
            final Status status,
            final Coordinates coordinates,
            final Instant createdAt,
            final Instant updatedAt,
            final UserId adminUserId,
            final UserId createdBy,
            final UserId lastModifiedBy,
            final String statusChangeReason,
            final Instant statusChangedAt
    ) {
        this.departmentId = departmentId;
        this.address = address;
        this.departmentCode = departmentCode;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.departmentType = departmentType;
        this.status = status;
        this.coordinates = coordinates;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminUserId = adminUserId;
        this.createdBy = createdBy;
        this.lastModifiedBy = lastModifiedBy;
        this.statusChangeReason = statusChangeReason;
        this.statusChangedAt = statusChangedAt;
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
            final CountryCode countryCode,
            final DepartmentType departmentType
    ) {
        Validate.notNull(departmentCode, "Department code cannot be null");
        Validate.notNull(city, "City cannot be null");
        Validate.notNull(street, "Street cannot be null");
        Validate.notNull(postalCode, "Postal code cannot be null");
        Validate.notNull(taxId, "Tax ID cannot be null");
        Validate.notNull(telephoneNumber, "Telephone number cannot be null");
        Validate.notNull(openingHours, "Opening hours cannot be null");
        Validate.notNull(email, "Email cannot be null");
        Validate.notNull(countryCode, "Country code cannot be null");
        Validate.notNull(departmentType, "Department type cannot be null");
        this.address = new Address(city, street, postalCode, countryCode);
        this.departmentCode = departmentCode;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.departmentType = departmentType;
        this.status = Status.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.adminUserId = null;
        this.createdBy = DomainRegistry.authenticationService().currentUser();
        this.lastModifiedBy = null;
        this.coordinates = DomainRegistry.departmentCoordinatesServicePort()
                .getCoordinates(getAddress());
    }

    public Department(
            final DepartmentId departmentId,
            final DepartmentCode departmentCode,
            final String city,
            final String street,
            final String postalCode,
            final TaxId taxId,
            final String telephoneNumber,
            final String openingHours,
            final String email,
            final CountryCode countryCode,
            final DepartmentType departmentType
    ) {
        Validate.notNull(departmentId, "Department id cannot be null");
        Validate.notNull(departmentCode, "Department code cannot be null");
        Validate.notNull(city, "City cannot be null");
        Validate.notNull(street, "Street cannot be null");
        Validate.notNull(postalCode, "Postal code cannot be null");
        Validate.notNull(taxId, "Tax ID cannot be null");
        Validate.notNull(telephoneNumber, "Telephone number cannot be null");
        Validate.notNull(openingHours, "Opening hours cannot be null");
        Validate.notNull(email, "Email cannot be null");
        Validate.notNull(countryCode, "Country code cannot be null");
        Validate.notNull(departmentType, "Department type cannot be null");
        this.departmentId = departmentId;
        this.address = new Address(city, street, postalCode, countryCode);
        this.departmentCode = departmentCode;
        this.taxId = taxId;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.departmentType = departmentType;
        this.status = Status.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.adminUserId = null;
        this.createdBy = DomainRegistry.authenticationService().currentUser();
        this.lastModifiedBy = null;
        this.coordinates = DomainRegistry.departmentCoordinatesServicePort()
                .getCoordinates(getAddress());
    }

    public DepartmentSnapshot snapshot() {
		return new DepartmentSnapshot(departmentId, departmentCode, address, taxId, telephoneNumber, openingHours, email,
				departmentType, status, coordinates, operatorId(), createdAt, updatedAt, adminUserId, createdBy,
				lastModifiedBy, statusChangeReason, statusChangedAt);
    }

    public void changeCoordinates(final Coordinates coordinates) {
        this.coordinates = coordinates;
        markAsModified();
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

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public void assignDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
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

    public void setAdminUserId(final UserId adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(final Coordinates coordinates) {
        this.coordinates = coordinates;
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

    public void changeStatus(final Status targetStatus, final DepartmentStatusChange change) {
        if (this.status == Status.DELETED && targetStatus != Status.DELETED) {
            throw new DepartmentStatusChangeForbiddenException(
                    "Department " + departmentCode.getValue() + " cannot change status because it is deleted");
        }
        if (targetStatus == Status.ACTIVE) {
            if (!change.activationBlockers().isEmpty()) {
                throw new DepartmentStatusChangeForbiddenException("Department " + departmentCode.getValue()
                        + " cannot be activated: " + String.join("; ", change.activationBlockers()));
            }
        } else {
            final DepartmentStatusPolicy policy = change.policy();
            if (targetStatus == Status.ARCHIVED && policy.archivingPrevented()) {
                throw new DepartmentStatusChangeForbiddenException("Department " + departmentCode.getValue()
                        + " cannot be archived because archiving is disabled");
            }
            if (targetStatus == Status.DELETED && policy.deletionPrevented()) {
                throw new DepartmentStatusChangeForbiddenException("Department " + departmentCode.getValue()
                        + " cannot be deleted because deletion is disabled");
            }
            if (policy.statusChangeReasonRequired() && change.reason() == null) {
                throw new DepartmentStatusChangeForbiddenException("Department " + departmentCode.getValue()
                        + " status change to " + targetStatus + " requires a reason");
            }
            if (policy.deactivationWithActiveWorkBlocked() && !change.activeWork().isEmpty()) {
                throw new DepartmentStatusChangeForbiddenException("Department " + departmentCode.getValue()
                        + " has active work: " + String.join("; ", change.activeWork()));
            }
        }
        this.status = targetStatus;
        if (targetStatus == Status.DELETED) {
            this.adminUserId = null;
        }
        this.statusChangeReason = change.reason();
        this.statusChangedAt = Instant.now();
        this.lastModifiedBy = change.modifiedBy();
        markAsModified();
    }


    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public String getStatusChangeReason() {
        return statusChangeReason;
    }

    public Instant getStatusChangedAt() {
        return statusChangedAt;
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
        final UserId currentUser = DomainRegistry.authenticationService().currentUser();
        if (currentUser.equals(adminUserId) || !adminUserId.isAdmin()) {
            throw new WrongUserAssignedException("User cannot be assigned as admin user for department");
        }
        this.adminUserId = adminUserId;
        markAsModified();
    }

    public void changeEmail(final String email) {
        this.email = email;
        markAsModified();
    }
}
