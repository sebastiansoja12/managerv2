package com.warehouse.supplier.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierAddRequest {

    private String firstName;

    private String lastName;

    private String telephone;

    private String depotCode;
}
