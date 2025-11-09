package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.event.DepartmentActivated;
import com.warehouse.department.domain.event.DepartmentCreated;
import com.warehouse.department.domain.event.DepartmentDeactivated;
import com.warehouse.department.domain.event.DepartmentTypeChanged;
import com.warehouse.department.domain.exception.ForbiddenDepartmentTypeException;
import com.warehouse.department.domain.registry.DomainRegistry;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentSnapshot;

import java.time.Instant;

public class Department {

    private DepartmentCode departmentCode;

    private Address address;

    private String nip;

    private String telephoneNumber;

    private String openingHours;
    
    private String email;

    private Boolean active;

    private DepartmentType departmentType;

    private Instant createdAt;

    private Instant updatedAt;

    public Department() {
    }

	public Department(final DepartmentCode departmentCode, final String city, final String street, final String country,
			final String postalCode, final String nip, final String telephoneNumber, final String openingHours,
			final String email, final Boolean active, final CountryCode countryCode,
			final DepartmentType departmentType, final Instant createdAt, final Instant updatedAt) {
		this.address = new Address(city, street, country, postalCode, countryCode);
        this.departmentCode = departmentCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.departmentType = departmentType;
    }

	public Department(final DepartmentCode departmentCode, final String city, final String street, final String country,
			final String postalCode, final String nip, final String telephoneNumber, final String openingHours,
			final String email, final Boolean active, final CountryCode countryCode,
			final DepartmentType departmentType) {
		this.address = new Address(city, street, country, postalCode, countryCode);
        this.departmentCode = departmentCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.departmentType = departmentType;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        DomainRegistry.publish(new DepartmentCreated(this.snapshot(), Instant.now()));
    }

    public DepartmentSnapshot snapshot() {
		return new DepartmentSnapshot(departmentCode, address, nip, telephoneNumber, openingHours, email, active);
    }

    public String getCity() {
        return address.city();
    }

    public String getStreet() {
        return address.street();
    }

    public String getCountry() {
        return address.country();
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

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
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

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public void changeAddress(final Address address) {
        this.address = address;
        markAsModified();
    }

    public void changeIdentificationNumber(final String newIdentificationNumber) {
        this.nip = newIdentificationNumber;
        markAsModified();
    }

    public void activate() {
        this.active = true;
        markAsModified();
        DomainRegistry.publish(new DepartmentActivated(this.snapshot(), Instant.now()));
    }

    public void deactivate() {
        this.active = false;
        markAsModified();
        DomainRegistry.publish(new DepartmentDeactivated(this.snapshot(), Instant.now()));
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
}
