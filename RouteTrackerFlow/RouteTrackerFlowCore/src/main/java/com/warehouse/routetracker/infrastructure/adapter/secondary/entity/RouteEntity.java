package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Document(collection = "route")
public class RouteEntity {

    @Id
    @GeneratedValue
    private String id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "parcel_id", nullable = false)
    private Long parcelId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;
}

