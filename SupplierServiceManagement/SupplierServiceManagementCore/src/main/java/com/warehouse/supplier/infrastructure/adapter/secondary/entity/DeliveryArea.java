package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;

import java.util.Set;

@Embeddable
public record DeliveryArea(

        @Column(name = "area_name")
        String areaName,

        @Column(name = "city")
        String city,

        @Column(name = "district")
        String district,

        @Column(name = "municipality")
        String municipality,

        @Column(name = "region")
        String region,

        @Column(name = "country")
        String country,

        @ElementCollection
        @CollectionTable(name = "delivery_area_postal_codes",
                joinColumns = @JoinColumn(name = "delivery_area_id"))
        @Column(name = "postal_code")
        Set<String> postalCodes
) {
    public DeliveryArea() {
        this(null, null, null, null, null, null, null);
    }
}
