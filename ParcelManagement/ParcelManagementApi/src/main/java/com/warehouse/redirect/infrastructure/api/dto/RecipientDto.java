package com.warehouse.redirect.infrastructure.api.dto;

import lombok.Data;


@Data
public class RecipientDto {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
