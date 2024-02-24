package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserDto {

    String username;

    String firstName;

    String lastName;

    String email;

    String role;

    String depotCode;
}
