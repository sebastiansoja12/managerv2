package com.warehouse.supplier.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supplier {

    private String supplierCode;

    private String firstName;

    private String lastName;

    private String telephone;

    private String depotCode;

    private Boolean active;

    public void updateSupplierCode(String generate) {
        this.supplierCode = supplierCode;
    }
}
