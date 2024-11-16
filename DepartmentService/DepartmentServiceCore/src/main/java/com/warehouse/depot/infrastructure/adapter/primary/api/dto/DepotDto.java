package com.warehouse.depot.infrastructure.adapter.primary.api.dto;

import lombok.Value;

@Value
public class DepotDto {

    DepotCodeDto depotCode;

    String city;

    String street;

    String country;

    String postalCode;

    String nip;

    String telephoneNumber;

    String openingHours;
}
