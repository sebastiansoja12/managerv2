package com.warehouse.depot.domain.vo;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class Depot {

    String city;

    String street;

    String country;

    String depotCode;

    String postalCode;

    String nip;

    String telephoneNumber;

    String openingHours;
}
