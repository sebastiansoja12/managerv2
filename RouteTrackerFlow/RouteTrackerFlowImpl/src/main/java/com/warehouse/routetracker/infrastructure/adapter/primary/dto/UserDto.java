package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import lombok.Value;

@Value
public class UserDto {
    String username;

    String firstName;

    String email;

    String depotCode;
}
