package com.warehouse.supplier.dto;

import lombok.Data;

@Data
public class SupplierAddResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String telephone;

    private String supplierCode;

    private String depotCode;
}
