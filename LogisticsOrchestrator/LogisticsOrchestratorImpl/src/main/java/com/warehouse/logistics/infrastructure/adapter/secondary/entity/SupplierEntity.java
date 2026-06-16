package com.warehouse.logistics.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentId;
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

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId departmentId;

    public boolean isActive() {
        return active;
    }

}
