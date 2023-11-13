package com.warehouse.returning.domain.model;

import lombok.Builder;

@Builder
public class Sender {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
