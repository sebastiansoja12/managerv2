package com.warehouse.returning.infrastructure.adapter.secondary.entity;


import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "returning")
@Data
public class ReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parcel_id", nullable = false)
    private Long parcelId;

    @Column(name = "return_status", nullable = false)
    private ReturnStatus returnStatus;

    @Column(name = "return_token", nullable = false)
    private String returnToken;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;
}
