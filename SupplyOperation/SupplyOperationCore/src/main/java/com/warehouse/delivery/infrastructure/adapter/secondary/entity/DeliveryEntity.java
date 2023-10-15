package com.warehouse.delivery.infrastructure.adapter.secondary.entity;


import java.time.LocalDateTime;

import com.warehouse.delivery.infrastructure.adapter.secondary.enumeration.Status;

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
}
