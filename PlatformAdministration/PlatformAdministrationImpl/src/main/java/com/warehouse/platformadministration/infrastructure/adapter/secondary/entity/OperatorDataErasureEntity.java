package com.warehouse.platformadministration.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operator_data_erasures")
public class OperatorDataErasureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "requested_by", nullable = false)
    private String requestedBy;

    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;

    @Column(name = "users_anonymized", nullable = false)
    private int usersAnonymized;

    @Column(name = "couriers_anonymized", nullable = false)
    private int couriersAnonymized;

    @Column(name = "devices_retired", nullable = false)
    private int devicesRetired;
}
