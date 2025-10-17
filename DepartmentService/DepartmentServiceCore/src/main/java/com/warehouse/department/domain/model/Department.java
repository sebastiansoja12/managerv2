package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.event.DepartmentCreated;
import com.warehouse.department.domain.registry.DomainRegistry;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentSnapshot;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;

import java.time.Instant;

public class Department {

    private DepartmentCode departmentCode;

    private Address address;

    private String nip;

    private String telephoneNumber;

    private String openingHours;

    private Boolean active;

    private CountryCode countryCode;

    private DepartmentType departmentType;

    private Instant createdAt;

    private Instant updatedAt;

    public Department() {
    }

    public Department(final DepartmentCode departmentCode, final String city, final String street, final String country,
                      final String postalCode, final String nip, final String telephoneNumber, final String openingHours,
                      final Boolean active, final CountryCode countryCode, final DepartmentType departmentType, 
                      final Instant createdAt, final Instant updatedAt) {
        this.address = new Address(city, street, country, postalCode);
        this.departmentCode = departmentCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.active = active;
        this.countryCode = countryCode;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.departmentType = departmentType;
    }

    public Department(final DepartmentCode departmentCode, final String city, final String street, final String country,
                     final String postalCode, final String nip, final String telephoneNumber, final String openingHours,
                     final Boolean active, final CountryCode countryCode, final DepartmentType departmentType) {
        this.address = new Address(city, street, country, postalCode);
        this.departmentCode = departmentCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.active = active;
        this.countryCode = countryCode;
        this.departmentType = departmentType;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        DomainRegistry.publish(new DepartmentCreated(this.snapshot(), Instant.now()));
    }

	public static Department from(final DepartmentEntity department) {
		if (department == null) {
			return null;
		} else {
			return new Department(new DepartmentCode(department.getDepartmentCode().getValue()), department.getCity(),
					department.getStreet(), department.getCountry(), department.getPostalCode(), department.getNip(),
					department.getTelephoneNumber(), department.getOpeningHours(), department.isActive(),
					department.getCountryCode(), DepartmentType.valueOf(department.getDepartmentType().name()),
					department.getCreatedAt(), department.getUpdatedAt());
		}

	}

    private DepartmentSnapshot snapshot() {
		return new DepartmentSnapshot(departmentCode, address, nip, telephoneNumber, openingHours, active, countryCode);
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
        return countryCode;
    }

    public void setCountryCode(final CountryCode countryCode) {
        this.countryCode = countryCode;
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

    public void updateStreet(final String street) {
        this.address = new Address(this.address.city(), street, this.address.country(), this.address.postalCode());
    }
}
