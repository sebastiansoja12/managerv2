package com.warehouse.department.domain.model;

public class Department {

    private String city;

    private String street;

    private String country;

    private String departmentCode;

    private String postalCode;

    private String nip;

    private String telephoneNumber;

    private String openingHours;

    private Boolean active;

    public Department() {
    }

    public Department(String city, String street, String country, String departmentCode, String postalCode, String nip, String telephoneNumber, String openingHours,
                      final Boolean active) {
        this.city = city;
        this.street = street;
        this.country = country;
        this.departmentCode = departmentCode;
        this.postalCode = postalCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.active = active;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void updateStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public void setStreet(final String street) {
        this.street = street;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }
}
