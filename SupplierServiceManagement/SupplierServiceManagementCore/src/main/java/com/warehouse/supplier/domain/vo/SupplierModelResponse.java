package com.warehouse.supplier.domain.vo;


import lombok.Value;

@Value
public class SupplierModelResponse {

	Long id;

	String supplierCode;

	String firstName;

	String lastName;

	String telephone;

	String depotCode;

	Long supplierTokenServiceId;
}
