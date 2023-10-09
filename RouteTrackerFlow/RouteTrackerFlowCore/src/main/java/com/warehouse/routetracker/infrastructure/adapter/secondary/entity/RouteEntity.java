package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import java.time.LocalDateTime;

import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "route")
@NamedEntityGraph(name = "RouteEntity.full", attributeNodes = {
        @NamedAttributeNode("user"),
        @NamedAttributeNode("parcel"),
        @NamedAttributeNode("depot"),
        @NamedAttributeNode("supplier")
})
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcel_id", referencedColumnName = "id")
    private ParcelEntity parcel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_code", referencedColumnName = "depot_code")
    private DepotEntity depot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_code", referencedColumnName = "supplier_code")
    private SupplierEntity supplier;


    public void updateParcelStatus(Status newStatus) {
        if (this.parcel != null) {
            this.parcel.setStatus(newStatus);
        }
    }
}

