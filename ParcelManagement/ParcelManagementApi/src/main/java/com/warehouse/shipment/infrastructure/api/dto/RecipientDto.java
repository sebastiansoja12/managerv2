package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipientDto {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
