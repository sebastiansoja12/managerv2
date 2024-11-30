package com.warehouse.delivery.infrastructure.adapter.secondary.entity;

import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DepotEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "delivery.SupplierEntity")
@Builder
@Table(name = "supplier")
@NamedEntityGraph(name = "delivery.SupplierEntity.full", attributeNodes = {
        @NamedAttributeNode("depot")
})
public class SupplierEntity {

    @Id
    @Column(name = "supplier_code", nullable = false, unique = true)
    private String supplierCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_code", referencedColumnName = "depot_code")
    private DepotEntity depot;

    public boolean isActive() {
        return active;
    }

}
