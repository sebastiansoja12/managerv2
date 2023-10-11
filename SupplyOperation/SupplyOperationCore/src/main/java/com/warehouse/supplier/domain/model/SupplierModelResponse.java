package com.warehouse.supplier.domain.model;


import lombok.Data;

@Data
public class SupplierModelResponse {

    private Long id;

    private String supplierCode;

    private String firstName;

    private String lastName;

    private String telephone;

    private String depotCode;

    private Long supplierTokenServiceId;
}
