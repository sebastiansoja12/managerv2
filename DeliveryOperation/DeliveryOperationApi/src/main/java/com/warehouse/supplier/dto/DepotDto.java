package com.warehouse.supplier.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class DepotDto {

    String city;

    String street;

    String country;

    String depotCode;

}
