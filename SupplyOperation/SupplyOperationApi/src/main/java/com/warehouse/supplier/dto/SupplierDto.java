package com.warehouse.supplier.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SupplierDto {

    String supplierCode;

    String firstName;

    String lastName;

    String telephone;

    String depotCode;
}
