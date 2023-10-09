package com.warehouse.routetracker.infrastructure.api.dto;

import lombok.Data;

@Data
public class SenderDto {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
