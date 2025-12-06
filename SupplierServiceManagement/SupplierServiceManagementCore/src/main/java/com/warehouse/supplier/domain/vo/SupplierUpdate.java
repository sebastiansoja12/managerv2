package com.warehouse.supplier.domain.vo;

import lombok.Value;

@Value
public class SupplierUpdate {

    String firstName;

    String lastName;

    String telephone;

    String supplierCode;

    Boolean active;
}
