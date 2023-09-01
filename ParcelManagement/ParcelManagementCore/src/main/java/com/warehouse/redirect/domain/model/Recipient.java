package com.warehouse.redirect.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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