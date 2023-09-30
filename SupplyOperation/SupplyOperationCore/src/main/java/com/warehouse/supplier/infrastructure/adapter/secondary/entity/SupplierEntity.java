package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "supplier.SupplierEntity")
@Builder
@Table(name = "supplier")
@NamedEntityGraph(name = "SupplierEntity.full", attributeNodes = {
        @NamedAttributeNode("depot")
})
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_code", nullable = false, unique = true)
    private String supplierCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depot_code", referencedColumnName = "depot_code")
    private DepotEntity depot;

}
