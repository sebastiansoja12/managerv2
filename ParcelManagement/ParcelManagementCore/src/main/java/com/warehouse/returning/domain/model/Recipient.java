package com.warehouse.returning.domain.model;

import lombok.Builder;


@Builder
public class Recipient {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}