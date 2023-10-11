package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    String username;

    String firstName;

    String lastName;

    String email;

    String role;

    String depotCode;
}
