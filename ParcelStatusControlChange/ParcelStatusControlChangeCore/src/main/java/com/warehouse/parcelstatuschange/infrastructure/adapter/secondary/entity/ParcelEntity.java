package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.enumeration.Status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parcel")
@Entity(name = "parcelStatus.ParcelEntity")
@EntityListeners(AuditingEntityListener.class)
public class ParcelEntity {

    @Id
    private Long id;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}