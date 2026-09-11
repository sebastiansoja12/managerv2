package com.warehouse.voronoi.domain.model;


import lombok.Builder;


@Builder
public class Department {
    private String city;

    private String street;

    private String country;

    private String departmentCode;

    private Coordinates coordinates;

    public Department(final String city,
                      final String street,
                      final String country,
                      final String departmentCode,
                      final Coordinates coordinates) {
        this.city = city;
        this.street = street;
        this.country = country;
        this.departmentCode = departmentCode;
        this.coordinates = coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}