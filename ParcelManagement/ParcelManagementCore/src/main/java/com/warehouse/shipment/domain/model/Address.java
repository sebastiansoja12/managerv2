package com.warehouse.shipment.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {
    String city;
    String street;
    String postalCode;
}
