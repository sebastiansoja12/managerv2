package com.warehouse.reroute.infrastructure.adapter.primary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class SenderDto {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
