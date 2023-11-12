package com.warehouse.reroute.domain.model;

import lombok.*;

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
