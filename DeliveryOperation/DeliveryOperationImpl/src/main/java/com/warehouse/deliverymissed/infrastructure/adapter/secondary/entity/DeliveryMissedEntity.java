package com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.DeliveryStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "department_code", nullable = false)
    private String departmentCode;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;

    @Column(name = "token", nullable = false)
    private String token;
}
