package com.warehouse.auth.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthenticationResponse {
    String authenticationToken;
}
