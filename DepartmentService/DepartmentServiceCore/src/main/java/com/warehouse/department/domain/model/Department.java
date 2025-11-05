package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.event.DepartmentCreated;
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

    public void changeAddress(final Address address) {
        final Address current = this.address;

        this.address = new Address(
                address.city() != null ? address.city() : current.city(),
                address.street() != null ? address.street() : current.street(),
                address.country() != null ? address.country() : current.country(),
                address.postalCode() != null ? address.postalCode() : current.postalCode(),
                address.countryCode() != null ? address.countryCode() : current.countryCode()
        );
    }
}
