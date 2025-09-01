package com.warehouse.department.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CountryCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "department.DepartmentEntity")
@Builder
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

    @Column(name = "country_code", nullable = false)
    @Enumerated(EnumType.STRING)
    private CountryCode countryCode;

}
