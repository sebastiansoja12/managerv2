package com.warehouse.parcelstatuschange.infrastructure.adapter.primary.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderDto {
    String firstName;
    String lastName;
    String email;
    String telephoneNumber;
    String city;
    String postalCode;
    String street;
}
