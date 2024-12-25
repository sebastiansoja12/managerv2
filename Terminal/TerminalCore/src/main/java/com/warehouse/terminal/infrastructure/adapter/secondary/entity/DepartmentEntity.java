package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity(name = "device.DepartmentEntity")
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "country", nullable = false)
    private String country;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final String departmentCode, final String city, final String street, final String country) {
        this.departmentCode = departmentCode;
        this.city = city;
        this.street = street;
        this.country = country;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String depotCode) {
        this.departmentCode = depotCode;
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
}

