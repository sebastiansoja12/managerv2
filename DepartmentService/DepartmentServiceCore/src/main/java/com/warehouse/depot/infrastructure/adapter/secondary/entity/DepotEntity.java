package com.warehouse.depot.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "depot.DepotEntity")
@Builder
@Table(name = "depot")
public class DepotEntity {

    @Id
    @Column(name = "depot_code", nullable = false, unique = true)
    private String depotCode;

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

}
