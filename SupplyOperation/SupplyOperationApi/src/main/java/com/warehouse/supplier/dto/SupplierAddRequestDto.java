package com.warehouse.supplier.dto;

import lombok.Data;

@Data
public class SupplierAddRequestDto {

    private String firstName;

    private String lastName;

    private String telephone;

    private String depotCode;
}