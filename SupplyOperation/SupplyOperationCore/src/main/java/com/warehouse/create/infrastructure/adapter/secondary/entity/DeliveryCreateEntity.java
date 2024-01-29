package com.warehouse.create.infrastructure.adapter.secondary.entity;

import com.warehouse.delivery.infrastructure.adapter.secondary.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "deliveryCreate.DeliveryEntity")
@Builder
@Table(name = "delivery")
public class DeliveryCreateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "parcel_id", nullable = false)
    private Long parcelId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "delivery_status", nullable = false)
    private Status deliveryStatus;

    @Column(name = "token")
    private String token;
}
