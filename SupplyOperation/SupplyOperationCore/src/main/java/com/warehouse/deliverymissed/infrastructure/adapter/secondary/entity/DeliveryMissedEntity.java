package com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "deliveryMissed.DeliveryEntity")
@Builder
@Table(name = "delivery")
public class DeliveryMissedEntity {

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

    @Column(name = "token", nullable = false)
    private String token;
}
