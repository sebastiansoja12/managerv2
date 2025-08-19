package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.DepartmentCode;
import jakarta.persistence.*;


@Entity(name = "shipment.DepartmentEntity")
@Table(name = "department")
public class DepartmentEntity {

    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    @EmbeddedId
    private DepartmentCode departmentCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "country", nullable = false)
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "nip", nullable = false)
    private String nip;

    @Column(name = "telephone_number", nullable = false)
    private String telephoneNumber;

    @Column(name = "opening_hours", nullable = false)
    private String openingHours;

    @Column(name = "active", nullable = false)
    private Boolean active;
    
    public DepartmentEntity() {
    }

	public DepartmentEntity(final Boolean active, final String city, final Country country,
			final DepartmentCode departmentCode, final String nip, final String openingHours, final String postalCode,
			final String street, final String telephoneNumber) {
        this.active = active;
        this.city = city;
        this.country = country;
        this.departmentCode = departmentCode;
        this.nip = nip;
        this.openingHours = openingHours;
        this.postalCode = postalCode;
        this.street = street;
        this.telephoneNumber = telephoneNumber;
    }

    public Boolean isActive() {
        return active;
    }

    public String getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
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
}
