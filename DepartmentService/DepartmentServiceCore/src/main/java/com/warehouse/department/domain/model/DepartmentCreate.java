package com.warehouse.department.domain.model;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.vo.DepartmentCode;

public class DepartmentCreate {
    private DepartmentCode departmentCode;

    private String city;

    private String street;

    private String postalCode;

    private String nip;

    private String telephoneNumber;

    private String openingHours;

    private String email;

    private CountryCode countryCode;
    
    private DepartmentType departmentType;

    public DepartmentCreate(final DepartmentCode departmentCode,
                            final String city,
                            final String street,
                            final String postalCode,
                            final String nip,
                            final String telephoneNumber,
                            final String openingHours,
                            final String email,
                            final CountryCode countryCode,
                            final DepartmentType departmentType) {
        this.departmentCode = departmentCode;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.countryCode = countryCode;
        this.departmentType = departmentType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
