package com.warehouse.auth.infrastructure.adapter.primary.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserResponseDto {
    String username;
    String depotCode;
    boolean nonExpired;
    boolean enabled;
    boolean nonLocked;
}
