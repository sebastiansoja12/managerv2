package com.warehouse.documentation.infrastructure.adapter.primary.api.dto;

import lombok.Builder;


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
