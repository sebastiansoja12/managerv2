package com.warehouse.shipment.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sender {

    private String firstName;
    private String lastName;
    private String email;
    private String telephoneNumber;
    private String city;
    private String postalCode;
    private String street;
}
