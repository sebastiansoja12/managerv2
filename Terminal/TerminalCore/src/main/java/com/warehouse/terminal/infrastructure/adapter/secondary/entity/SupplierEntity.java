package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "device.SupplierEntity")
@Builder
@Table(name = "supplier")
@NamedEntityGraph(name = "device.SupplierEntity.full", attributeNodes = {
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
    private DepartmentEntity depot;

}