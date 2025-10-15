package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.vo.DepartmentCode;

public class DepartmentCreate {
    private DepartmentCode departmentCode;

    private String city;

    private String street;

    private String country;

    private String postalCode;

    private String nip;

    private String telephoneNumber;

    private String openingHours;

    private CountryCode countryCode;
    
    private DepartmentType departmentType;

	public DepartmentCreate(final String city, final String country,
			final CountryCode countryCode, final DepartmentCode departmentCode, final String nip,
			final String openingHours, final String postalCode, final String street, final String telephoneNumber,
			final DepartmentType departmentType) {
		this.city = city;
		this.country = country;
		this.countryCode = countryCode;
		this.departmentCode = departmentCode;
		this.nip = nip;
		this.openingHours = openingHours;
		this.postalCode = postalCode;
		this.street = street;
		this.telephoneNumber = telephoneNumber;
		this.departmentType = departmentType;
	}

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(final CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(final String nip) {
        this.nip = nip;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(final String openingHours) {
        this.openingHours = openingHours;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(final String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(final DepartmentType departmentType) {
        this.departmentType = departmentType;
    }
}
