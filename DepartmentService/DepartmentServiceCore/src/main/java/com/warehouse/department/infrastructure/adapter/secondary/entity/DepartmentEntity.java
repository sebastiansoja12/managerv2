package com.warehouse.department.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;

import com.warehouse.department.infrastructure.adapter.secondary.enumeration.DepartmentType;
import jakarta.persistence.*;

import java.time.Instant;


@Entity(name = "department.DepartmentEntity")
@Table(name = "department")
public class DepartmentEntity {

    @EmbeddedId
    @Column(name = "department_code", nullable = false, unique = true)
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "nip", nullable = false)
    private String nip;

    @Column(name = "telephone_number", nullable = false)
    private String telephoneNumber;

    @Column(name = "opening_hours", nullable = false)
    private String openingHours;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "country_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode countryCode;

    @Column(name = "department_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DepartmentType departmentType;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public DepartmentEntity() {
    }

    public DepartmentEntity(final DepartmentCode departmentCode,
                            final String city,
                            final String street,
                            final String country,
                            final String postalCode,
                            final String nip,
                            final String telephoneNumber,
                            final String openingHours,
                            final String email,
                            final Boolean active,
                            final CountryCode countryCode,
                            final DepartmentType departmentType,
                            final Instant createdAt,
                            final Instant updatedAt) {
        this.departmentCode = departmentCode;
        this.city = city;
        this.street = street;
        this.country = country;
        this.postalCode = postalCode;
        this.nip = nip;
        this.telephoneNumber = telephoneNumber;
        this.openingHours = openingHours;
        this.email = email;
        this.active = active;
        this.countryCode = countryCode;
        this.departmentType = departmentType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Boolean isActive() {
        return active;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public String getNip() {
        return nip;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public DepartmentType getDepartmentType() {
        return departmentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }
}
