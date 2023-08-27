package com.warehouse.delivery.infrastructure.adapter.secondary.entity;


import java.time.LocalDateTime;
import java.util.UUID;

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
	UUID id;

    @Column(name = "parcel_id", nullable = false)
    Long parcelId;

    @Column(name = "created", nullable = false)
    LocalDateTime created;

    @Column(name = "depot_code", nullable = false)
    String depotCode;

    @Column(name = "supplier_code", nullable = false)
    String supplierCode;

    @Column(name = "delivery_status", nullable = false)
    Status deliveryStatus;
}
