package com.warehouse.supplier.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class SupplierUpdateRequestDto {

    String firstName;

    String lastName;

    String telephone;

    String supplierCode;

    String depotCode;

    Boolean active;
}
