package com.warehouse.auth.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserResponse {
    String username;
    String depotCode;
    boolean nonExpired;
    boolean enabled;
    boolean nonLocked;
}
