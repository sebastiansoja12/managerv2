package com.warehouse.logistics.infrastructure.adapter.secondary.entity;


import java.time.LocalDateTime;

import com.warehouse.logistics.infrastructure.adapter.secondary.enumeration.Status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "delivery.DeliveryEntity")
@Builder
@Table(name = "delivery")
public class DeliveryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

    @Column(name = "shipment_id", nullable = false)
    private Long parcelId;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "department_code", nullable = false)
    private String depotCode;

    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    @Column(name = "delivery_status", nullable = false)
    private Status deliveryStatus;

    @Column(name = "token", nullable = false)
    private String token;
}
