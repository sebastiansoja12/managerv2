package com.warehouse.parcelstatuschange.domain.vo;

import lombok.*;

@Value
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
