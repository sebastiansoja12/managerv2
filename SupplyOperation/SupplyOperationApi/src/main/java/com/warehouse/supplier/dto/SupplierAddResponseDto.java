package com.warehouse.supplier.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SupplierAddResponseDto {

    String firstName;

    String lastName;

    String telephone;

    String supplierCode;

    String depotCode;
}
