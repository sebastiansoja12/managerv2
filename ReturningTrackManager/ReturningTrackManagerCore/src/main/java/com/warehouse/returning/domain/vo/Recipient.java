package com.warehouse.returning.domain.vo;

import lombok.Builder;
import lombok.Value;


@Value
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